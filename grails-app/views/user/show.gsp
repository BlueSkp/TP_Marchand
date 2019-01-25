<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'user.label', default: 'User')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>

        <asset:stylesheet src="tabulation.css"/>
        <asset:stylesheet src="scrollbar.css"/>
    </head>

    <body>
        <a href="#show-user" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="show-user" class="content scaffold-show" role="main">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            %{--<f:display bean="user" except="password" />--}%


        <div class="container_user">
        <div class="column">
            <ol class="property-list">
                <li class="fieldcontain">
                    <span id="userUsername-label" class="property-label">Username</span>
                    <div class="property-value" aria-labelledby="userUsername-label">${user.username}</div>
                </li>

                <li class="fieldcontain">
                    <span id="userFirstName-label" class="property-label">First Name</span>
                    <div class="property-value" aria-labelledby="userFirstName-label">${user.firstName}</div>
                </li>

                <li class="fieldcontain">
                    <span id="userLastName-label" class="property-label">Last Name</span>
                    <div class="property-value" aria-labelledby="userLastName-label">${user.lastName}</div>
                </li>

                <li class="fieldcontain">
                    <span id="userMail-label" class="property-label">Mail</span>
                    <div class="property-value" aria-labelledby="userMail-label">${user.mail}</div>
                </li>
            </ol>
        </div>
            <div class="column">
            <ol class="property-list">

                <li class="fieldcontain">
                    <span id="userTel-label" class="property-label">Telephone</span>
                    <div class="property-value" aria-labelledby="userTel-label">${user.tel}</div>
                </li>

                <li class="fieldcontain">
                    <span id="userDoB-label" class="property-label">Date of Birth</span>
                    <div class="property-value" aria-labelledby="userDoB-label">${user.dob}</div>
                </li>

                <li class="fieldcontain">
                    <span id="membre-label" class="property-label">Membre du groupe</span>
                    <div class="property-value" aria-labelledby="membre-label">
                        <g:each in="${roleList}" var="role">
                            <g:link controller="role" action="show" id="${role.id}">
                                ${role.authority},
                            </g:link>
                        </g:each>
                    </div>
                </li>
            </ol>
            </div>
        </div>

            <div class="tab">
                <button class="tablinks" onclick="openMessage(event, 'Recu')" id="defaultOpen" >Messages Reçus</button>
                <button class="tablinks" onclick="openMessage(event, 'Envoye')">Messages Envoyés</button>

            </div>

            <div id="Envoye" class="tabcontent">
                <div class="scroll">
                %{--<li class="fieldcontain">--}%
                    %{--<h1>Messages Envoyés</h1>--}%
                    %{--<div class="property-value" aria-labelledby="membre-label">--}%
                        <g:each in="${messageEnvoyeList}" var="message" status="i">
                            <ol class="property-list message">

                            <li class="fieldcontain">
                                <span class="property-label">Message</span>
                            <g:link controller="message" action="show" id="${message.id}">
                                ${message.messageContent},
                            </g:link>
                            </li>

                            <li class="fieldcontain">
                                <span class="property-label">Date Created</span>
                                <div class="property-value" aria-labelledby="messageDate-label">${message.dateCreated}</div>
                            </li>

                            <li class="fieldcontain">
                                <span id="destinataire-label" class="property-label">Destinataire</span>
                                <div class="property-value" aria-labelledby="destinataire-label">
                                        <g:each in="${destinatairesMessageEnvoyeList[i]}" var="destinataireMessage">
                                            <g:link controller="user" action="show" id="${destinataireMessage.id}">
                                                ${destinataireMessage.firstName+" "+destinataireMessage.lastName},
                                            </g:link>
                                        </g:each>
                                        %{--<g:each controller="user" action="getListDestinataires" var="destinataireMessage">--}%
                                            %{--<g:link controller="user" action="show" id="${destinataireMessage.id}">--}%
                                                %{--${destinataireMessage.firstName+" "+destinataireMessage.lastName},--}%
                                            %{--</g:link>--}%
                                        %{--</g:each>--}%
                                </div>
                            </li>
                            </ol>
                        </g:each>
                    %{--</div>--}%
                %{--</li>--}%

            %{--</ol>--}%
                </div>
        </div>

        <div id="Recu" class="tabcontent">
            <div class="scroll">

                %{--<li class="fieldcontain">--}%
                    %{--<h1>Messages Reçus</h1>--}%
                    %{--<div class="property-value" aria-labelledby="membre-label" >--}%
                        <g:each in="${messageRecuList.reverse()}" var="message">

                            <ol class="property-list message">
                            <li class="fieldcontain">
                                <span class="property-label">Message</span>
                                <g:link controller="message" action="show" id="${message.id}">
                                    ${message.messageContent},
                                </g:link>

                            <li class="fieldcontain">
                                <span class="property-label">Date Created</span>
                                <div class="property-value" aria-labelledby="messageDate-label">${message.dateCreated}</div>
                            </li>

                            <li class="fieldcontain">
                                <span id="author-label" class="property-label">Author</span>
                                <div class="property-value" aria-labelledby="destinataire-label">
                                    <g:link controller="user" action="show" id="${message.author.id}">
                                        ${message.author.firstName+" "+message.author.lastName},
                                    </g:link>
                                </div>
                            </li>
                            </ol>
                        </g:each>
                    %{--</div>--}%
                %{--</li>--}%

            %{--</ol>--}%
            </div>
        </div>

            <g:form resource="${this.user}" method="DELETE">
                <fieldset class="buttons">
                    <g:link class="edit" action="edit" resource="${this.user}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
                    <input class="delete" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                </fieldset>
            </g:form>
        </div>

    </body>
</html>
