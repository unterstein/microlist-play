;(function($) {
    $.fn.setCursorPosition = function(position){
        if(this.length == 0) return this;
        return $(this).setSelection(position, position);
    };

    $.fn.setSelection = function(selectionStart, selectionEnd) {
        if(this.length == 0) return this;
        input = this[0];

        if (input.createTextRange) {
            var range = input.createTextRange();
            range.collapse(true);
            range.moveEnd('character', selectionEnd);
            range.moveStart('character', selectionStart);
            range.select();
        } else if (input.setSelectionRange) {
            input.focus();
            input.setSelectionRange(selectionStart, selectionEnd);
        }

        return this;
    };

    $.fn.focusEnd = function() {
        this.setCursorPosition(this.val().length);
    };

    $.fn.selectAll = function() {
        this.setSelection(0, this.val().length);
    };
})(jQuery);

function handleCaretPositions() {
    $(".nav input").selectAll();
}

$(function() {
    handleCaretPositions();
});

function doAfterAjaxHandling() {
    $(function() {
      handleCaretPositions();
    });
}

function endsWith(element, suffix) {
    return $(element).val().toLowerCase().match(suffix.toLowerCase() + "$") == suffix.toLowerCase();
}

function toggle(element) {
    $(element).slideToggle(300);
}

function ajaxCall(route, successFn, errorFn, data) {
    route.ajax({
        success: function(data) { if(successFn) successFn(data); },
        error:   function(data) { if(errorFn) errorFn(data); },
        data: data
    });
}

function updateElement(element, data) {
    $(element).html(data);
    doAfterAjaxHandling();
}
