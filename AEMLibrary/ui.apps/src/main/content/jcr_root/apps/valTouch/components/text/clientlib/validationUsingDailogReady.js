(function ($, $document) {
    "use strict";
    $document.on("dialog-ready", function() {
        $(window).adaptTo("foundation-ui").alert("Open", "Dialog now open, event [dialog-ready]");
        var $form = $(this).closest("form.foundation-form"),
            title = $form.find("[name='./email']"),
            message, clazz = "coral-Button ";
        $("[name='./email']").hide();
    });

})($, $(document));;