<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'message.label', default: 'Message')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#create-message" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="create-message" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
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
            <g:form resource="${this.message}" method="POST">
                <fieldset class="form">
                    %{--<f:all bean="message"/>--}%

                    <fieldset class="form">
                        %{--<div class="fieldcontain required">--}%
                            %{--<label for="messageContent">Message Content <span class="required-indicator">*</span> </label>--}%
                            %{--<input type="text" name="messageContent" value="" required="" id="messageContent">--}%
                        %{--</div>--}%
                    <div class="fieldcontain required">
                        <label for="messageContent">Author <span class="required-indicator">*</span> </label>
                        <input type="text" name="messageContent" value="${authorInstance.firstName}" required="" id="authorInstance">
                    </div>

                    <div class="fieldcontain required">
                        <label for="messageContent">Destinataires <span class="required-indicator">*</span> </label>
                        <input type="text" name="destinataire" value="${destinataireList.mail}" required="" id="destinataires">
                    </div>

                    <div class="fieldcontain required">
                        <label for="messageContent">Groupes <span class="required-indicator">*</span> </label>
                        <input type="text" name="groupes" value="${groupeList.authority}" required="" id="groupes">
                    </div>

                    <div class="fieldcontain required">
                        <label for="messageContent">Message Content <span class="required-indicator">*</span> </label>
                        <g:textArea name="messageContent" value="" rows="5" cols="40" id="messageContent"/>
                    </div>

                    %{--<div class="fieldcontain">--}%
                        %{--<label for="author">Author <span class="required-indicator">*</span></label>--}%
                            %{--<select name="author.id" id="author">--}%
                            %{--<g:each in="${user.id}" var="user">--}%
                                    %{--<option value="${user.id}">${user.firstName+" "+user.lastName}</option>--}%
                            %{--</g:each>--}%

                            %{--</select>--}%
                    %{--</div>--}%
                    </fieldset>


                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>

    </body>
</html>
