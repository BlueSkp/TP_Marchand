package fr.mbds.tp

import grails.plugin.springsecurity.annotation.Secured
import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*
@Secured('ROLE_ADMIN')
class MessageController {

    MessageService messageService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond messageService.list(params), model:[messageCount: messageService.count()]
    }

    def show(Long id) {
        def messageInstance = Message.get(id)
        def userMessageList = UserMessage.findAllByMessage(messageInstance)
        def userList = userMessageList.collect{it.user}
        respond messageInstance, model: [userList: userList]

        //respond Message.get(id)
    }

    def create() {
//      SpringSecurityService.getPrincipale()
        def authorInstance = User.get(principal.id)
        if (!authorInstance)
            authorInstance = User.get(10)
        def destinataireList = User.getAll()
        def groupeList = Role.getAll()
        respond new Message(params), model: [authorInstance : authorInstance,destinataireList: destinataireList,groupeList: groupeList]

//      respond new Message(params)
    }

    def save(Message message) {
        if (message == null) {
            notFound()
            return
        }

        try {
            messageService.save(message)
        } catch (ValidationException e) {
            respond message.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = "Le message a été correctement sauvegardé (id : ${message.id})"
                redirect message
            }
            '*' { respond message, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond messageService.get(id)
    }

    def update(Message message) {
        if (message == null) {
            notFound()
            return
        }

        try {
            messageService.save(message)
        } catch (ValidationException e) {
            respond message.errors, view:'edit'
            return
        }

        // Recuperer le destinataire avec l'id du destinataire
        // Instancier ce dernier
        def destinataire = User.get(params.test)


        // Creer une instance de UserMessage correspondant à l'envoie
        // de ce message
        // Persister l'instance UserMessage nouvellement créée
        UserMessage userMessageInstance = UserMessage.create(destinataire,message,true);


        //Si groupe spécidifié:
        //Recuperer l'instance de Role désigné
        // Créer un nouveau UserMessage pour tous les utilisateurs dudit Groupe

        request.withFormat {
            form multipartForm {
                flash.message = "Le message a été correctement mis à jour (id : ${message.id})"
                redirect message
            }
            '*'{ respond message, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        messageService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'message.label', default: 'Message'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
