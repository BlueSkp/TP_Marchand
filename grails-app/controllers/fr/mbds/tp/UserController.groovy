package fr.mbds.tp

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException

import javax.management.relation.RoleList

import static org.springframework.http.HttpStatus.*
@Secured('ROLE_ADMIN')
class UserController {

    UserService userService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond userService.list(params), model:[userCount: userService.count()]
    }

    def show(Long id) {
        def userInstance = User.get(id)
        def userRoleList = UserRole.findAllByUser(userInstance)
        def roleList = userRoleList.collect{it.role}

//        les messages envoyés, quand et à qui
        def messageEnvoyeList = Message.findAllByAuthor(userInstance)
        def destinatairesMessageEnvoyeList = []
        messageEnvoyeList.each{
            def userMessageList = UserMessage.findAllByMessage(it)
            destinatairesMessageEnvoyeList.add(userMessageList.collect{it.user})
        }
//        les messages recus, quand et par qui
        def messageRecuList = UserMessage.findAllByUser(userInstance).collect{it.message}

        respond userInstance, model: [roleList: roleList, messageEnvoyeList: messageEnvoyeList,destinatairesMessageEnvoyeList:destinatairesMessageEnvoyeList,messageRecuList:messageRecuList]
        //respond userService.get(id)
    }

    def create() {
        respond new User(params)
    }

    def save(User user) {
        if (user == null) {
            notFound()
            return
        }

        try {
            userService.save(user)
            def roles = params.roles
            if (roles != null) {
                roles.each {
                    def role = Role.findByAuthority(it)
                    UserRole.create(user, role, true)
                }
            }

        } catch (ValidationException e) {
            respond user.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect user
            }
            '*' { respond user, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond userService.get(id)
    }

    def update(User user) {
        if (user == null) {
            notFound()
            return
        }

        try {
            userService.save(user)
        } catch (ValidationException e) {
            respond user.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'user.label', default: 'User'), user.id])
                redirect user
            }
            '*'{ respond user, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        userService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'user.label', default: 'User'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'user.label', default: 'User'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
