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
        console.log("blubb");
        console.log(data);
    }, null, $(element).parents("form").serialize());
}