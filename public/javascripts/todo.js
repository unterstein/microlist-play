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
        if(this.val() != undefined) {
            this.setSelection(0, this.val().length);
        }
    };

    $.fn.endsWith = function(suffix) {
        if(this.val() != undefined) {
            return this.val().toLowerCase().match(suffix.toLowerCase() + "$") == suffix.toLowerCase();
        } else {
            return false;
        }
    }
})(jQuery);

function handleCaretPositions() {
    $(".nav input:visible").selectAll();
    $(".task input:visible").selectAll();
}

$(function() {
    doAfterAjaxHandling();
});

function doAfterAjaxHandling() {
    $(function() {
      handleCaretPositions();
      $('.alert-success').delay(5000).fadeToggle(1500);
    });
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

function replaceElement(element, data) {
    $(element).replaceWith(data);
    doAfterAjaxHandling();
}
