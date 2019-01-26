package fr.mbds.tp

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*
@Secured('ROLE_ADMIN')
class RoleController {

    RoleService roleService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond roleService.list(params), model:[roleCount: roleService.count()]
    }

    def show(Long id) {
        def roleInstance = Role.get(id)
        def userRoleList = UserRole.findAllByRole(roleInstance)
        def userList = userRoleList.collect{it.user}
        respond roleInstance, model: [userList: userList]
        //respond roleService.get(id)
    }

    def create() {
        respond new Role(params)
    }

    def save(Role role) {
        if (role == null) {
            notFound()
            return
        }

        try {
            roleService.save(role)
            def membres = params.membres
            if (membres != null) {
                if (membres.getClass()!=String) {
                    membres.each {
                        def membre = User.findByUsername(it)
                        UserRole.create(membre, role, true)
                    }
                }else{
                    def membre = User.findByUsername(membres)
                    UserRole.create(membre, role, true)
                }
            }
        } catch (ValidationException e) {
            respond role.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'role.label', default: 'Role'), role.id])
                redirect role
            }
            '*' { respond role, [status: CREATED] }
        }
    }

    def edit(Long id) {
        def roleInstance = Role.get(id)
        def userRoleList = UserRole.findAllByRole(roleInstance)
        def userList = userRoleList.collect{it.user}
        respond roleInstance, model: [userList: userList]

        //respond roleService.get(id)
    }

    def update(Role role) {
        if (role == null) {
            notFound()
            return
        }

        try {
            roleService.save(role)

            def updatedMembresList = []

            def membres = params.membres
            if (membres != null) {
                if (membres.getClass()!=String) {
                    membres.each {
                        def membre = User.findByUsername(it)
                        updatedMembresList.add(membre)
//                        UserRole.create(membre, role, true)
                    }
                }else{
                    def membre = User.findByUsername(membres)
                    updatedMembresList.add(membre)
//                    UserRole.create(membre, role, true)
                }
            }

            def oldUserList = UserRole.findAllByRole(role).collect{it.user}

            updatedMembresList.minus(oldUserList).each{
                UserRole.create(it, role, true)
            }
            oldUserList.minus(updatedMembresList).each{
                UserRole.findByUserAndRole(it,role).delete(flush:true)
            }



        } catch (ValidationException e) {
            respond role.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'role.label', default: 'Role'), role.id])
                redirect role
            }
            '*'{ respond role, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        //roleService.delete(id)
        def roleInstance = Role.get(id)

        if (roleInstance) {

            // On recupere la liste des UserRole qui referencent le role que nous souhaitons effacer
            def userRoles = UserRole.findAllByRole(roleInstance)
            // On itere sur la liste et efface chaque reference
            userRoles.each {
                UserRole userRole ->
                    userRole.delete(flush: true)
            }

            //on peut enfin effacer l'instance de User
            roleInstance.delete(flush: true)
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'role.label', default: 'Role'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'role.label', default: 'Role'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
