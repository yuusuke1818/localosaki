/*
 * 
 * 
 */

$(function() {
    $(document).on("dragstart", function( e, ui ){
        var id = e.target.id;
        var draggable;
        var dropTarget;
        if( id.substr(id.length - 9) === 'dragPanel' ){
            //子要素
            draggable = e.target;
            dropTarget = $(draggable).parent().children()[$(draggable).index()-1];
        }else if( id.substr(id.length - 13) === 'dragPanelOpen' 
                || id.substr(id.length - 14) === 'dragPanelClose' 
                ){
            //親要素
            draggable = $(e.target).closest('div[id$=collapsiblePanel]')[0];
            dropTarget = $(draggable).parent().children()[$(draggable).index()-1];
        }else{
            return;
        }
        $(draggable).addClass('drag_and_drop_source_invisible');
        $(dropTarget).addClass('drag_and_drop_source_invisible');
    });
    $(document).on("dragstop", function( e, ui ){
        //ドロップ領域の表示を解除
        $('.rf-drp-hvr').removeClass('rf-drp-hvr');
        //非表示領域の回復
        $('.drag_and_drop_source_invisible').removeClass('drag_and_drop_source_invisible');
    });
});

