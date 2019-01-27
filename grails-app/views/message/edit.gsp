<%@ page import="fr.mbds.tp.Role" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'message.label', default: 'Message')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#edit-message" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="edit-message" class="content scaffold-edit" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.message}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.message}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.message}" method="PUT">
                <g:hiddenField name="version" value="${this.message?.version}" />
                <fieldset class="form">
                    %{--<f:all bean="message"/>--}%

                    <p> Le message a déjà été envoyé, vous ne pouvez plus le modifier. Vous pouvez l'envoyer à des destinataires supplémentaires.</p>
                    <ol class="property-list message">

                        <li class="fieldcontain">
                            <label>Author</label>
                            <div class="property-value" aria-labelledby="author-label">
                                <g:link controller="user" action="show" id="${message.author.id}">
                                    ${message.author.firstName+" "+message.author.lastName}
                                </g:link>
                            </div>
                        </li>

                    <li class="fieldcontain">
                        <label>Destinataire</label>
                        <div class="property-value" aria-labelledby="destinataire-label">
                            <g:each in="${userList}" var="user">
                                <g:link controller="user" action="show" id="${user.id}">
                                    ${user.firstName+" "+user.lastName},
                                </g:link>
                            </g:each>
                        </div>
                    </li>

                        <div class="fieldcontain required">
                            <label>Destinataires additionels </label>
                            <select class="js-example-basic-multiple" name="destinataires" multiple="multiple" style="width: 60%">
                               <g:each in="${fr.mbds.tp.User.list().minus(userList)}">
                                    <option>${it.username}</option>
                                </g:each>
                            </select>
                        </div>

                        <div class="fieldcontain required">
                            <label>Groupes</label>
                            <select class="js-example-basic-multiple" name="groupes" multiple="multiple" style="width: 60%">
                                <g:each in="${fr.mbds.tp.Role.list()}">
                                    <option>${it.authority}</option>
                                </g:each>
                            </select>
                        </div>

                        <li class="fieldcontain">
                            <label>Message Content</label>
                            <div class="property-value" aria-labelledby="messageContent-label">${message.messageContent}</div>
                        </li>

                        <li class="fieldcontain">
                            <label>Date Created</label>
                            <div class="property-value" aria-labelledby="messageDate-label">${message.dateCreated}</div>
                        </li>

                    </ol>


                </fieldset>
                <fieldset class="buttons">
                    <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
