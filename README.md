# TP_Marchand
**TP Grails/WebService MBDS**

Ici se trouvent des points à préciser sur l'application. 
**Ce git correspond à la réalisation des 2 TP demandés.**

**PARTIE I : TP Création d'une plateforme de gestion de Messages**

1.Seul un membre du groupe ROLE_ADMIN peut se connecter. Par défaut il n'y en a qu'un :

        Nom d'utilisateur : `admin`
        
        Mot de passe : `secret`


2.Cette application permet de faire les actions de CRUD sur les entités "user" et "role".

2.1 USER :
- CREATE : on peut ajouter le user à 1/des groupes si souhaité
- READ : **il y a un système de tab permettant de voir les messages "reçus pour" ou "envoyés par" l'utilisateur.** Vous pouvez scroller au niveau des messages pour voir les plus anciens.
- UPDATE : on peut modifier les informations du user ET mofifier ses appartenances à des groupes (en enlever, en rajouter)
- DELETE : supprimer un utilisateur supprimera le user des tables "USER","USER_ROLE" et "USER_MESSAGE". Par contre, ses messages envoyés ne seront pas supprimés mais l'auteur sera remplacé par un "deleted_user".
           on ne peut supprimer le user "deleted_user"
           
2.2 ROLE :
- CREATE : on peut créer un role et y rajouter des membres
- READ : on peut voir les informations du role et ses membres
- UPDATE : on peut modifier le nom du role, et les membres qui en font partie (en enlever, en rajouter)
- DELETE : on peut supprimer le role

3.On peut aussi faire les actions CRU(D) sur "message"
- CREATE : un formulaire permet de créer un message avec un auteur, plusieurs destinataires et groupes destinataires.
           **Le cas où un destinataire serait présent plusieurs fois (par exemple il est membre de plusieurs groupes) EST pris en compte.**
           J'utilise un multiselect proposé par "select2" `https://select2.org/`
- READ : on peut voir les informations du messages ainsi que les destinataires du message
- UPDATE : on ne peut pas modifier le message (car il a déjà été envoyé) **MAIS on peut rajouter de nouveaux destinataires**. Les destinataires ayant déjà recu le message ne le recevront pas à nouveau.   
- DELETE : on ne peut pas supprimer un message (on peut depuis l'API car c'est demandé dans les consignes du TP) car c'est aux utilisateurs de supprimer leurs propres messages si ils le souhaitent (fonctionnalité non prise en compte ici)


**PARTIE II : TP WebServices REST**

1. La collection POSTMAN est disponible sur le git "Postman Collections". Il faut tourner le test avant d’avoir modifié la base de données, au risque que certains éléments aient été modifiés ou n’existent plus.
Le document de spécification est aussi sur le git, "specification_MARCHAND.pdf"

2. Les points d'entrée sur User et Message permettent de faire des GET/POST/PUT/DELETE.

2.1 Le DELETE du user supprime toutes traces du user dans les tables "USER", "USER_ROLE" et "USER_MESSAGE". Si il est auteur d'un message,l'auteur sera remplacé par le "deleted_user".

3. Les points d'entrée messageToUser et messageToGroup font appel à des fonctions "createNewUserMessage" et "createNewGroupeMessage" écrites en dehors des 2 fonctions précédentes pour éviter la répétition de code.
