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
    ajaxCall(jsRoutes.controllers.Authentication.registerUser(), function(data) {
        if(data[0] == '/') {
            window.location.href=data;
        } else {
            updateElement($(element).parents("form"), data);
        }
    }, null, $(element).parents("form").serialize());
}