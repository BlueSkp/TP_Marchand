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
        def destinatairesMessageEnvoyeList = [][]
        def i=0
        messageEnvoyeList.each{
            def userMessageList = UserMessage.findAllByMessage(it)
//            destinatairesMessageEnvoyeList.add([])
            destinatairesMessageEnvoyeList.add(userMessageList.collect{it.user})
            i=i+1
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
                if (roles.getClass()!=String) {
                    roles.each {
                        def role = Role.findByAuthority(it)
                        UserRole.create(user, role, true)
                    }
                }else{
                    def role = Role.findByAuthority(roles)
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
        def userInstance = User.get(id)
        def userRoleList = UserRole.findAllByUser(userInstance)
        def roleList = userRoleList.collect{it.role}

        respond userInstance, model: [roleList: roleList]

        //respond userService.get(id)
    }

    def update(User user) {
        if (user == null) {
            notFound()
            return
        }

        try {
            userService.save(user)

            def updatedRolesList = []

            def roles = params.roles
            if (roles != null) {
                if (roles.getClass()!=String) {
                    roles.each {
                        def role = Role.findByAuthority(it)
                        updatedRolesList.add(role)
//                        UserRole.create(membre, role, true)
                    }
                }else{
                    def role = Role.findByAuthority(roles)
                    updatedRolesList.add(role)
//                    UserRole.create(membre, role, true)
                }
            }

            def oldRolesList = UserRole.findAllByUser(user).collect{it.role}

            updatedRolesList.minus(oldRolesList).each{
                UserRole.create(user, it, true)
            }
            oldRolesList.minus(updatedRolesList).each{
                UserRole.findByRoleAndUser(it,user).delete(flush:true)
            }


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

        // userService.delete(id)
        def userInstance = User.get(id)

        if (userInstance) {
            // On recupere la liste des UserMessage qui referencent le user que nous souhaitons effacer
            def userMessages = UserMessage.findAllByUser(userInstance)
            // On itere sur la liste et efface chaque reference
            userMessages.each {
                UserMessage userMessage ->
                    userMessage.delete(flush: true)
            }

            // On recupere la liste des UserRole qui referencent le user que nous souhaitons effacer
            def userRoles = UserRole.findAllByUser(userInstance)
            // On itere sur la liste et efface chaque reference
            userRoles.each {
                UserRole userRole ->
                    userRole.delete(flush: true)
            }

            // On recupere la liste des Messages qui referencent le user/autheur que nous souhaitons effacer
            def messages = Message.findAllByAuthor(userInstance)
            // On itere sur la liste et rends l'autheur à null pour chaque message
            messages.each {
                Message messageToUpdate ->
                    messageToUpdate.setAuthor(User.findByUsername("deleted_user"))
            }

            //on peut enfin effacer l'instance de User
            userInstance.delete(flush: true)
        }


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

//    def getListDestinataires(){
//        listDestinataires = UserMessage.findAllByMessage(params.message).collect{it.user}
//        return listDestinataires
//    }
}
