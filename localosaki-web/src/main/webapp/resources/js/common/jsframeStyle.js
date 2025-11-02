/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function getOriginalStyle(fApr) {

    fApr.titleBarCaptionFontSize = '14px';
    fApr.titleBarCaptionFontWeight = 'normal';
    fApr.titleBarHeight = '26px';

    fApr.onInitialize = function () {
        var partsBuilder = fApr.getPartsBuilder();

        {
            //configure close button appearance[begin]//////////////
            var cbApr = partsBuilder.buildTextButtonAppearance();

            cbApr.size = 20;
            cbApr.width = 26;
            cbApr.captionFontRatio = 0.85;
            cbApr.backgroundColorDefault = '#d0d0d0';
            cbApr.backgroundColorFocused = '#fc615c';
            cbApr.backgroundColorHovered = cbApr.backgroundColorFocused;
            cbApr.backgroundColorPressed = cbApr.backgroundColorDefault;

            cbApr.borderStyleDefault = 'solid';
            cbApr.borderStyleFocused = cbApr.borderStyleDefault;
            cbApr.borderStyleHovered = cbApr.borderStyleDefault;
            cbApr.borderStylePressed = cbApr.borderStyleDefault;

            //background
            cbApr.backgroundColorDefault = '#d0d0d0';
            cbApr.backgroundColorFocused = '#fc615c';
            cbApr.backgroundColorHovered = cbApr.backgroundColorFocused;
            cbApr.backgroundColorPressed = cbApr.backgroundColorDefault;


            var closeBtnEle = partsBuilder.buildTextButton(cbApr);
            var eleLeft = -8;
            var eleTop = -24;
            var eleAlign = 'RIGHT_TOP';

            fApr.addFrameComponent('closeButton', closeBtnEle, eleLeft, eleTop, eleAlign);

            //拡大ボタン
            //configure expansion button appearance[begin]//////////////
            var ebApr = partsBuilder.buildTextPlusButtonAppearance();

            ebApr.size = 20;
            ebApr.width = 26;
            //ebApr.height = 18;
            ebApr.captionFontRatio = 1.10;
            ebApr.backgroundColorDefault = '#d0d0d0';
            ebApr.backgroundColorFocused = '#ffe4c4';
            ebApr.backgroundColorHovered = ebApr.backgroundColorFocused;
            ebApr.backgroundColorPressed = ebApr.backgroundColorDefault;

            ebApr.borderStyleDefault = 'solid';
            ebApr.borderStyleFocused = ebApr.borderStyleDefault;
            ebApr.borderStyleHovered = ebApr.borderStyleDefault;
            ebApr.borderStylePressed = ebApr.borderStyleDefault;

            //background
            ebApr.backgroundColorDefault = '#d0d0d0';
            ebApr.backgroundColorFocused = '#ffe4c4';
            ebApr.backgroundColorHovered = ebApr.backgroundColorFocused;
            ebApr.backgroundColorPressed = ebApr.backgroundColorDefault;

            var expanBtnEle = partsBuilder.buildTextButton(ebApr);
            var expanLeft = -50;
            var expanTop = -24;
            var expanAlign = 'RIGHT_TOP';

            fApr.addFrameComponent('expansionButton', expanBtnEle, expanLeft, expanTop, expanAlign);

            //縮小ボタン
            //configure shrink button appearance[begin]//////////////
            var sbApr = partsBuilder.buildTextMinusButtonAppearance();

            sbApr.size = 20;
            sbApr.width = 26;

            sbApr.captionFontRatio = 1.10;
            sbApr.backgroundColorDefault = '#d0d0d0';
            sbApr.backgroundColorFocused = '#a0d8ef';
            sbApr.backgroundColorHovered = sbApr.backgroundColorFocused;
            sbApr.backgroundColorPressed = sbApr.backgroundColorDefault;

            sbApr.borderStyleDefault = 'solid';
            sbApr.borderStyleFocused = sbApr.borderStyleDefault;
            sbApr.borderStyleHovered = sbApr.borderStyleDefault;
            sbApr.borderStylePressed = sbApr.borderStyleDefault;

            //background
            sbApr.backgroundColorDefault = '#d0d0d0';
            sbApr.backgroundColorFocused = '#a0d8ef';
            sbApr.backgroundColorHovered = sbApr.backgroundColorFocused;
            sbApr.backgroundColorPressed = sbApr.backgroundColorDefault;


            var shrinkBtnEle = partsBuilder.buildTextButton(sbApr);
            var shrinkLeft = -85;
            var shrinkTop = -24;
            var shrinkAlign = 'RIGHT_TOP';

            fApr.addFrameComponent('shrinkButton', shrinkBtnEle, shrinkLeft, shrinkTop, shrinkAlign);
        }
    }

    return fApr;
}