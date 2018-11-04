package tp1_v2

import fr.mbds.tp.Message
import fr.mbds.tp.Role
import fr.mbds.tp.User
import fr.mbds.tp.UserMessage
import fr.mbds.tp.UserRole

class BootStrap {

    def init = { servletContext ->
        def userAdmin = new User(username:"admin",password:"secret",firstName:"admin",lastName:"admin",mail:"admin@mali.com").save(flush:true,failOnError:true)
        def roleAdmin = new Role(authority:"ROLE_ADMIN").save(flush:true,failOnError:true)
        def rolePromo2019 = new Role(authority:"ROLE_PROMO2019").save(flush:true,failOnError:true)
        new Role(authority:"ROLE_MBDS").save(flush:true,failOnError:true)
        UserRole.create(userAdmin,roleAdmin,true)


        (1..50).each {
            def userInstance = new User(username:"username-$it",password:"password-$it",firstName:"first-$it", lastName:"last-$it",mail:"mail-$it").save(flush:true,failOnError:true)


            new Message(messageContent: "lala",author: userInstance).save(flush:true,failOnError:true)

            UserRole.create(userInstance,rolePromo2019,true)
        }

        Message.list().each{
            Message messageInstance ->

                User.list().each{
                    User userInstance ->
                        UserMessage.create(userInstance,messageInstance,true)
                }
        }

//        def userInstance = new User(username:"username",password:"password",firstName:"first", lastName:"last",mail:"mail")
//        def userInstance2 = new User(username:"username2",password:"password2",firstName:"first2", lastName:"last2",mail:"mail2")
//
//        userInstance.save(flush:true,failOnError:true)
//        userInstance2.save(flush:true,failOnError:true)
//
//        def messageInstance = new Message(messageContent: "lala",author: userInstance).save(flush:true)
//        UserMessage.create(userInstance2,messageInstance,true)

    }
    def destroy = {
    }
}
