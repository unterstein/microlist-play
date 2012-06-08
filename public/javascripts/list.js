function toggleText(element) {
    $(element).siblings("span").toggle();
    $(element).siblings("input").toggle();
    doAfterAjaxHandling();
}