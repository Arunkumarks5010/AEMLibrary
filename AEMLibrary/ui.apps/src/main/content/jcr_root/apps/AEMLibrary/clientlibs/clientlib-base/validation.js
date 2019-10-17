   (function($, window, document) {

    /* Adapting window object to foundation-registry */
    var registry = $(window).adaptTo("foundation-registry");

    /*Validator for TextField - Any Custom logic can go inside validate function - starts */
    /*https://stackoverflow.com/questions/49326696/aem-6-2-touch-ui-validation-on-text-field/52459861?source=post_page-----e78106012282----------------------#52459861*/   

	/* validation/granite-ID/granite-class must be added as a property to the field of dailog*/
       registry.register("foundation.validation.validator", {

        selector: "[data-validation=selected-option]",
        validate: function(el) {
            var element = $(el);
            var pattern = element.data('itemSelected');
            var value = element.val();
            var $form = element.closest("form.foundation-form")
            var email = $form.find("[name='./email']");
            if (value == 'multi') {
				console.log("value"+value);
               
        $("[name='./email']").parent().hide();
            } else {
                console.log("value"+value);
               
        $("[name='./email']").parent().show();



            }

        }
    });
})
($, window, document);
