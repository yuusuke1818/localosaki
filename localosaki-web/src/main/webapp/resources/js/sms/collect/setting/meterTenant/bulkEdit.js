// ファイルが選択されたときに、ファイル名を表示する
function changeFile() {
    var file = $('input[type=file]').prop('files')[0];
    $('.filename').html(htmlEscape(file.name));
    document.getElementById('tenantBulkBeanUL:file_input_name').value = file.name;
}

// input type="file" タグを再作成する（初期化のため）
function remakeInputFile() {
    var file = document.getElementById('tenantBulkBeanUL:file_input');
    file.parentNode.innerHTML = file.parentNode.innerHTML;
}

// ファイル名を初期化する
function clearFileName() {
    $('.filename').html('');
    document.getElementById('tenantBulkBeanUL:file_input_name').value = '';
}