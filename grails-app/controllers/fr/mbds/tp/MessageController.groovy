package fr.mbds.tp

import grails.converters.JSON
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
//        def authorInstance = User.get(principal.id)
//        if (!authorInstance)
//            authorInstance = User.get(10)
//        def destinataireList = User.getAll()
//        def groupeList = Role.getAll()
//        respond new Message(params), model: [authorInstance : authorInstance,destinataireList: destinataireList,groupeList: groupeList]

        respond new Message(params)
    }

    def save(Message message) {
        if (message == null) {
            notFound()
            return
        }

        try {
            //le select2 renvoi un string et non un objet, donc on récupère l'objet user/author ainsi :
            message.author= User.findByUsername(params.authorString)
            messageService.save(message)

            //liste de tous les destinataires à qui il faut envoyer le message, avec répition possible (car membres de plusieurs groupes par exemples)
            def listeNOTDistinctDestinataires = []

            def destinatairesM = params.destinataires
            if (destinatairesM != null) {
                if (destinatairesM.getClass()!=String){ //ce test est dans le cas qu'il n'y est qu'un seul element, au quel cas each diviserait l'élément caractere par caractere
                    destinatairesM.each {
                        def destinataireM = User.findByUsername(it)
                        listeNOTDistinctDestinataires.add(destinataireM)
                        //UserMessage.create(destinataire, message, true)
                    }
                }
                else{
                    def destinataireM = User.findByUsername(destinatairesM)
                    //UserMessage.create(destinataire, message, true)
                    listeNOTDistinctDestinataires.add(destinataireM)
                }

            }
            def groupes = params.groupes
            if (groupes != null) {
                if (groupes.getClass()!=String) {
                    groupes.each {
                        def groupe = Role.findByAuthority(it)
                        def membresGroupe = UserRole.findAllByRole(groupe).collect { it.user }
                        membresGroupe.each {
                            listeNOTDistinctDestinataires.add(it)
                            //UserMessage.create(it, message, true)
                        }
                    }
                }else{
                    def groupe = Role.findByAuthority(groupes)
                    def membresGroupe = UserRole.findAllByRole(groupe).collect { it.user }
                    membresGroupe.each {
                        //UserMessage.create(it, message, true)
                        listeNOTDistinctDestinataires.add(it)
                    }
                }

            }

            def listeDistinctDestinataires = listeNOTDistinctDestinataires.unique()
            listeDistinctDestinataires.each{
                UserMessage.create(it, message, true)
            }

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
