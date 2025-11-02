$(document).on("click", ".contents_accordion_header", function() {
    $(this).parent().children(".contents_accordion_body").slideToggle();
    $(".contents_accordion_header").find(".button_closer").toggle();
    $(".contents_accordion_header").find(".button_opener").toggle();
});
