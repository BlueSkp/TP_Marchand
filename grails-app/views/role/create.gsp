<%@ page import="fr.mbds.tp.User" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'role.label', default: 'Role')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
        <g:javascript library='jquery' />
    </head>
    <body>
        <a href="#create-role" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="create-role" class="content scaffold-create" role="main">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.role}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.role}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form resource="${this.role}" method="POST">
                <fieldset class="form">
                    %{--<f:all bean="role"/>--}%
                    <div class="fieldcontain required">
                        <label>Name <span class="required-indicator">*</span> </label>
                        <input type="text" name="authority" value="" required="" id="authority">
                    </div>

                    <div class="fieldcontain required">
                        <label>Membre(s) du groupe </label>
                        <select class="js-example-basic-multiple" name="membres" multiple="multiple" style="width: 60%" value="">
                            <g:each in="${fr.mbds.tp.User.list()}">
                                <option>${it.username}</option>
                            </g:each>
                        </select>
                    </div>


                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>

    </body>
</html>
