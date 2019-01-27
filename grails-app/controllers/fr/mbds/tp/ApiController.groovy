package fr.mbds.tp

import grails.converters.JSON
import grails.converters.XML

import javax.servlet.http.HttpServletRequest

class ApiController {

    def index() { render "ok" }

    def message() {
        switch (request.getMethod()) {
            case "GET":
                if (params.id)  // on doit retourner une instance de message
                {
                    def messageInstance = Message.get(params.id)
                    if (messageInstance)
                        reponseFormat(messageInstance, request)
                    else
                        response.status = 404

                } else            //on doit retourner la liste de tous les messages
                    forward action: "messages"
                break

            case "POST":
                forward action: "messages"
                break

            case "PUT":
                def messageInstance = params.id ? Message.get(params.id) : null
                if (messageInstance) {
                    if (params.get("author.id"))
                    {
                        def authorInstance = User.get((Long)params.get("author.id"))
                        if (authorInstance)
                            messageInstance.author=authorInstance
                    }
                    if (params.messageContent)
                        messageInstance.messageContent=params.messageContent
                    if (messageInstance.save(flush:true))
                        render(text:"Mise à jour effectuée pour le message ${messageInstance.id}")
                    else
                        render(status:400, text:"Echec de la mise à jour du message  ${messageInstance.id} ")
                }
                else
                    render (status:404,text:"Le message désigné est introuvable")
                break

            case "DELETE":
                def messageInstance = params.id ? Message.get(params.id) : null
                if (messageInstance) {
                    // On recupere la liste des UserMessage qui referencent le message que nous souhaitons effacer
                    def userMessages = UserMessage.findAllByMessage(messageInstance)
                    // On itere sur la liste et effce chaque reference
                    userMessages.each {
                        UserMessage userMessage ->
                            userMessage.delete(flush: true)
                    }
                    //on peut enfin effacer l'instance de Message
                    messageInstance.delete(flush: true)
                    //if (!messageInstance)
                        render(status: 200,text:"message effacé")
                }
                else
                    render(status:404,text:"message introuvable")
                break

            default:
                render(status:405,text:"Méthode non authorisée")
                break
        }
    }

    def messages() {
        switch (request.getMethod()) {
            case "GET":
                reponseFormatList(Message.list(), request)
                break
            case "POST":
                //Verifier auteur

                def authorInstance = params.author ? params.author.id ? User.get(params.author.id) : null : null
                def messageInstance
                if (authorInstance) {
                    //creer le message
                    messageInstance = new Message(author: authorInstance, messageContent: params.messageContent)
                    if (messageInstance.save(flush: true))

                        render(status: 201)
                }


                if (response.status != 201)
                    response.status = 400
                break
            default:
                render(status:405,text:"Méthode non authorisée")
                break
        }

    }

    def user() {
        switch (request.getMethod()) {
            case "GET":
                if (params.id)  // on doit retourner une instance de l'utilisateur
                {
                    def userInstance = User.get(params.id)
                    if (userInstance)
                        reponseFormat(userInstance, request)
                    else
                        response.status = 404

                } else            //on doit retourner la liste de tous les users
                    forward action: "users"
                break

            case "POST":
                forward action: "users"
                break

            case "PUT":
                def userInstance = params.id ? User.get(params.id) : null
                if (userInstance) {
                    if (params.username)
                        userInstance.username=params.username
                    if (params.password)
                        userInstance.password=params.password
                    if (params.get("firstName"))
                        userInstance.firstName=params.firstName
                    if (params.lastName)
                        userInstance.lastName=params.lastName
                    if (params.mail)
                        userInstance.mail=params.mail
                    if (params.tel)
                        userInstance.tel=params.tel
                    if (params.dob)
                        userInstance.dob=params.dob
                    if (userInstance.save(flush:true))
                        render(text:"Mise à jour effectuée pour le user ${userInstance.id}")
                    else
                        render(status:400, text:"Echec de la mise à jour du user  ${userInstance.id} ")
                }
                else
                    render (status:404,text:"Le user désigné est introuvable")
                break

            case "DELETE":
                def userInstance = params.id ? User.get(params.id) : null
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
                    // On itere sur la liste et remplace l'autheur par "deleted_user" pour chaque message
                    messages.each {
                        Message messageToUpdate ->
                            messageToUpdate.setAuthor(User.findByUsername("deleted_user"))
                    }

                    //on peut enfin effacer l'instance de User
                    userInstance.delete(flush: true)
                    //if (!messageInstance)
                    render(status: 200,text:"user effacé")
                }
                else
                    render(status:404,text:"user introuvable")
                break

            default:
                render(status:405,text:"Méthode non authorisée")
                break
        }
    }

    def users() {
        switch (request.getMethod()) {
            case "GET":
                reponseFormatList(User.list(), request)
                break
            case "POST":
                def dobInstance = params.dob ? params.dob : null
                def telInstance = params.tel ? params.tel : null
                def userInstance = new User(username:params.username,password:params.password,firstName:params.firstName,lastName:params.lastName,mail:params.mail,dob:dobInstance,tel:telInstance)
                if (userInstance.save(flush: true))
                    render(status: 201)
                if (response.status != 201)
                    response.status = 400
                break
            default:
                render(status:405,text:"Méthode non authorisée")
                break

        }

    }

    def messageToUser(){
        if (request.getMethod().equals("POST")) {
            def messageInstance

            if (params.messageId)  // on doit retourner une instance de message
            {
                if ( Message.get(params.messageId)) {
                    messageInstance =Message.get(params.messageId)
                    createNewUserMessage(params.receiverId, messageInstance)
                    if (response.status==201){
                        render(status: 201, text: "success")
                    }
                }else
                    render(status:404,text:"Le message n'existe pas (mauvais ID)")
            }else if (params.authorId&&params.messageContent){
                //Verifier auteur
                    def authorInstance = User.get(params.authorId)

                    if (authorInstance) {
                            //creer le message
                            messageInstance = new Message(author: authorInstance, messageContent: params.messageContent)
                            if (messageInstance.save(flush: true)) {
                                createNewUserMessage(params.receiverId, messageInstance)
                                if (response.status==201){
                                    render(status: 201, text: "success")
                                }

                            }
                            else
                                response.status = 400
                    } else
                        render(status:404,text:"Le message crée n'a pas d'auteur existant")
            } else
                render(status:400,text:"Il n'y a ni message désigné, ni les bons parametres pour creer un nouveau message ")

        } else
            render(status:405,text:"Méthode non authorisée")
    }

    def messageToGroup(){
        if (request.getMethod().equals("POST")) {
            def messageInstance
            def groupId = params.groupId ? Message.get(params.groupId) : null
            if (groupId) {
                if (params.messageId)  // on doit retourner une instance de message
                {
                    if (Message.get(params.messageId)) {
                        messageInstance = Message.get(params.messageId)
                        createNewGroupMessage(params.groupId, messageInstance)
                    } else
                        render(status: 404, text: "Le message n'existe pas (mauvais ID)")
                } else if (params.authorId && params.messageContent) {
                    //Verifier auteur
                    def authorInstance = User.get(params.authorId)

                    if (authorInstance) {
                        //creer le message
                        messageInstance = new Message(author: authorInstance, messageContent: params.messageContent)
                        if (messageInstance.save(flush: true))
                            createNewGroupMessage(params.groupId, messageInstance)
                        else
                            response.status = 400
                    } else
                        render(status: 404, text: "Le message crée n'a pas d'auteur existant")
                } else
                    render(status: 400, text: "Il n'y a ni message désigné, ni les bons parametres pour creer un nouveau message ")

            }else
                render(status: 404, text: "Group non existant")
        }
        else
            render(status: 405, text: "Méthode non authorisée")
    }


    //fonction qui crée les UserMessage
    def createNewUserMessage( idreceiver , Message messageInstance){
        // Ajouter destinataires
        if (idreceiver)
        {
            def receiverInstance = User.get(idreceiver)
            if (receiverInstance){
                if (new UserMessage(user: receiverInstance, message: messageInstance).save(flush:true)){
                    //render(status: 201,text:"succes")
                    response.status=201
                }
                else
                    response.status = 400
            }else
                render(status:404,text:"Le destinataire n'existe pas")
        }else
            render(status:400,text:"Il n'y a pas l'Id du destinataire")
    }

//fonction qui fait appel à la fonction createNewUserMessage pour creer les UserMessage de tout un groupe
    def createNewGroupMessage( idGroupe , Message messageInstance){
        // Ajouter destinataires
        if (idGroupe)
        {
            def roleInstance = Role.get(idGroupe)
            // On recupere la liste des UserRole du groupe Role voulu
            def userRoles = UserRole.findAllByRole(roleInstance)
            // On itere sur la liste et pour chaque user on creer un user message

            for ( i in 1..userRoles.size()) {
                createNewUserMessage( userRoles[i-1].getUser().getId() , messageInstance)
                if (response.status!=201)
                    //si l'un des enregistrement de UserMessage ne fonctionne pas, on arrete
                    break
            }
            if (response.status==201)
                //si tous les enregistrements ont fonctionné alors succes
                render(status:201,text:"Succes")
        }
    }


    def reponseFormat(Object instance, HttpServletRequest request) {
        switch (request.getHeader("Accept")) {
            case "text/xml":
            case "application/xml":
                render instance as XML
                break
            case "text/json":
                render instance as JSON
                break
            default:
                response.status = 415
                break
        }
    }

    def reponseFormatList(List list, HttpServletRequest request) {
        switch (request.getHeader("Accept")) {
            case "text/xml":
            case "application/xml":
                render list as XML
                break
            case "text/json":
                render list as JSON
                break
            default:
                response.status = 415
                break
        }
    }

}
