$(function() {
  if ($('#checkbox1').prop('checked')) {
    $('.accordion_content1').show();
  } else {
    $('.accordion_content1').hide();
  }
  if ($('#checkbox2').prop('checked')) {
    $('.accordion_content2').show();
  } else {
    $('.accordion_content2').hide();
  }
  if ($('#checkbox3').prop('checked')) {
    $('.accordion_content3').show();
  } else {
    $('.accordion_content3').hide();
  }
});

$(function() {
  $('#checkbox1').change(function() {
    if ($(this).prop('checked')) {
      $('.accordion_content3').hide("slow");
      $('#checkbox3').prop('checked', false);
      $('.accordion_content2').hide("slow");
      $('#checkbox2').prop('checked', false);

      $('.accordion_content1').show("slow");
    } else {
      $('.accordion_content1').hide("slow");
    }
  });
});

$(function() {
  $('#checkbox2').change(function() {
    if ($(this).prop('checked')) {
      $('.accordion_content1').hide("slow");
      $('#checkbox1').prop('checked', false);
      $('.accordion_content2').show("slow");
    } else {
      $('.accordion_content2').hide("slow");
      $('.accordion_content3').hide("slow");
      $('#checkbox3').prop('checked', false);
    }
  });
});

$(function() {
  $('#checkbox3').change(function() {
    if ($(this).prop('checked')) {
      $('.accordion_content3').show("slow");
    } else {
      $('.accordion_content3').hide("slow");
    }
  });
});