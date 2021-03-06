<%@ page import="fr.mbds.tp.User; fr.mbds.tp.Role" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'message.label', default: 'Message')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
        <g:javascript library='jquery' />
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
                    <div class="fieldcontain required">
                        <label>Author <span class="required-indicator">*</span> </label>
                        <select class="js-example-basic-multiple" name="authorString" style="width: 60%" optionValue="username" >
                            <g:each in="${fr.mbds.tp.User.list()}">
                                <option>${it.username}</option>
                            </g:each>
                        </select>
                    </div>

                    <div class="fieldcontain required">
                        <label>Destinataires  </label>
                        <select class="js-example-basic-multiple" name="destinataires" multiple="multiple" style="width: 60%">
                            <g:each in="${fr.mbds.tp.User.list()}">
                                <option>${it.username}</option>
                            </g:each>
                        </select>
                    </div>

                    <div class="fieldcontain required">
                        <label>Groupes</label>
                        <select class="js-example-basic-multiple" name="groupes" multiple="multiple" style="width: 60%">
                            <g:each in="${Role.list()}">
                                <option>${it.authority}</option>
                            </g:each>
                        </select>
                    </div>

                    <div class="fieldcontain required">
                        <label>Message Content <span class="required-indicator">*</span> </label>
                        <g:textArea name="messageContent" value="" rows="5" cols="40" id="messageContent"/>
                    </div>

                </fieldset>


                </fieldset>
                <fieldset class="buttons">
                    <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
                </fieldset>
            </g:form>
        </div>

        <content tag="javascript">
            <script type="text/javascript" src="${request.contextPath}js/samplePage.js"></script>
        </content>

    </body>
</html>
