function toggleText(element) {
    $(element).siblings("span").toggle();
    $(element).siblings("input").toggle();
    doAfterAjaxHandling();
}

function addProject(element) {
    ajaxCall(jsRoutes.controllers.TodoList.addProject(), function(data) {
        updateElement(element, data);
    });
}