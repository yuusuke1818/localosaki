function changeElectricPriceMenu(obj){
    var value = $(obj).val();
    var updateProcessFlg = $("#smsCollectSettingMeterTenantEditBean\\:updateProcessFlg").val();
    if (updateProcessFlg === "false" || value == 0){
        $("#smsCollectSettingMeterTenantEditBean\\:settingTypeButton_0").prop("disabled", true);
    } else {
        $("#smsCollectSettingMeterTenantEditBean\\:settingTypeButton_0").prop("disabled", false);
    }

}

function changeTenantNo() {
    var userCd = $("input[id$=userCode]").val();
    var tenantNo = $("input[id$=buildingNo]").val();
    var divisionBuildingNo = $("#smsCollectSettingMeterTenantEditBean\\:divisionBuildingNo").text();

    // ユーザーコードに入力があって、テナント番号に入力がない
    if (userCd !== "" && tenantNo === ""){
        $("input[id$=buildingNo]").val(divisionBuildingNo + "_" + userCd);
    }

}
