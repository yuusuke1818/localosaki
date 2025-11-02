window.onload = function() {
    setInitAccordion();
};

function setInitAccordion() {
    $(".contents_accordion_header").parent().children(
            ".contents_accordion_body").show();
    $(".contents_accordion_header").find(".button_opener").hide();
    $(".contents_accordion_header").find(".button_closer").show();
}

$(document).on('click', '.contents_accordion_header', function() {
    $(this).parent().children(".contents_accordion_body").slideToggle();
    $(this).find(".button_opener").toggle();
    $(this).find(".button_closer").toggle();
});
