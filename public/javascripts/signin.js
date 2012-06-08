function showRegisterPanel(element) {
    if(!$(element).find("form").length) {
        ajaxCall(jsRoutes.controllers.Authentication.registerPanel(), function(data) {
            updateElement(element, data);
        });
    } else {
        updateElement(element, "");
    }
}
function register(element) {
    ajaxCall(jsRoutes.controllers.Authentication.registerUserAjax(), function(data) {
        updateElement($(element).parents("form"), data);
    }, null, $(element).parents("form").serialize());
}