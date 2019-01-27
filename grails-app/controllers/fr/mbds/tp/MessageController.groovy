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
    }

    def create() {
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

                    }
                }
                else{
                    def destinataireM = User.findByUsername(destinatairesM)
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
                        }
                    }
                }else{
                    def groupe = Role.findByAuthority(groupes)
                    def membresGroupe = UserRole.findAllByRole(groupe).collect { it.user }
                    membresGroupe.each {
                        listeNOTDistinctDestinataires.add(it)
                    }
                }

            }

            //envoie des messages aux destinataires distincts
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
        def messageInstance = Message.get(id)
        def userMessageList = UserMessage.findAllByMessage(messageInstance)
        def userList = userMessageList.collect{it.user}
        respond messageInstance, model: [userList: userList]

    }

    def update(Message message) {
        if (message == null) {
            notFound()
            return
        }

        try {

            //messageService.save(message) (le messge en lui mêmene peut pas etre modifié

//            Destinataires additionels:
            //liste de tous les destinataires à qui il faut envoyer le message, avec répition possible (car membres de plusieurs groupes par exemples)
            def listeNOTDistinctDestinataires = []

            def destinatairesM = params.destinataires
            if (destinatairesM != null) {
                if (destinatairesM.getClass()!=String){ //ce test est dans le cas qu'il n'y est qu'un seul element, au quel cas each diviserait l'élément caractere par caractere
                    destinatairesM.each {
                        def destinataireM = User.findByUsername(it)
                        listeNOTDistinctDestinataires.add(destinataireM)
                    }
                }
                else{
                    def destinataireM = User.findByUsername(destinatairesM)
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
                        }
                    }
                }else{
                    def groupe = Role.findByAuthority(groupes)
                    def membresGroupe = UserRole.findAllByRole(groupe).collect { it.user }
                    membresGroupe.each {
                        listeNOTDistinctDestinataires.add(it)
                    }
                }

            }

            //envoie des messages
            def listeDistinctDestinataires = listeNOTDistinctDestinataires.unique()
            listeDistinctDestinataires.each{
                UserMessage.create(it, message, true)
            }


        } catch (ValidationException e) {
            respond message.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = "Le message a été correctement mis à jour (id : ${message.id})"
                redirect message
            }
            '*'{ respond message, [status: OK] }
        }
    }

    def delete(Long id) {
       // Le message a deja été envoyé, il ne peut pas être supprimé

      redirect action:"index", method:"GET"
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
