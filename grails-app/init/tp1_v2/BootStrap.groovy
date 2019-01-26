package tp1_v2

import fr.mbds.tp.Message
import fr.mbds.tp.Role
import fr.mbds.tp.User
import fr.mbds.tp.UserMessage
import fr.mbds.tp.UserRole

class BootStrap {

    def init = { servletContext ->
//        user et role ADMIN
        def userAdmin = new User(username:"admin",password:"secret",firstName:"admin",lastName:"admin",mail:"admin@mali.com").save(flush:true,failOnError:true)
        def roleAdmin = new Role(authority:"ROLE_ADMIN").save(flush:true,failOnError:true)
        UserRole.create(userAdmin,roleAdmin,true)

//        role, user et message pour Promo2019
        def rolePromo2019 = new Role(authority:"ROLE_PROMO2019").save(flush:true,failOnError:true)
        (1..25).each {
            def userInstance = new User(username:"username-$it",password:"password-$it",firstName:"first-$it", lastName:"last-$it",mail:"mail-$it").save(flush:true,failOnError:true)

            new Message(messageContent: "coucou, je suis ${userInstance.username}",author: userInstance).save(flush:true,failOnError:true)

            UserRole.create(userInstance,rolePromo2019,true)
        }

        Message.list().each{
            Message messageInstance ->

                User.list().each{
                    User userInstance ->
                        UserMessage.create(userInstance,messageInstance,true)
                }
        }

//      role MBDS et message, pour les 5 premiers users
        def roleMBDS = new Role(authority:"ROLE_MBDS").save(flush:true,failOnError:true)
        def messageInstanceMBDS =  new Message(messageContent: "Bienvenue en MBDS",author: userAdmin).save(flush:true,failOnError:true)
        def messageInstanceMBDS2 =  new Message(messageContent: "Dossiers d'inscription à rendre cette semaine DERNIER DELAI",author: userAdmin).save(flush:true,failOnError:true)
        User.list([max:5]).each {
            User userInstance ->
                UserRole.create(userInstance,roleMBDS,true)
                UserMessage.create(userInstance,messageInstanceMBDS, true)
                UserMessage.create(userInstance,messageInstanceMBDS2, true)
        }

        //placeholder user pour les users supprimés
        def deletedUser =new User(username:"deleted_user",password:"DONT_CONNECT_WITH_ME_123(*_*)çà%ùµ£¤°0O:D°*__",firstName:"deleted_user",lastName:"deleted_user",mail:"deleted_user").save(flush:true,failOnError:true)


    }
    def destroy = {
    }
}
