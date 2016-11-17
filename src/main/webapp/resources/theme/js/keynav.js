/**
 * 
 */
$(document).ready(function(e) {
	
	// Must do this to wait for datatable to load
	setTimeout(overrideTabCell, 1000);

});

/**
 * Override the current tabCell function
 */
var overrideTabCell = function(e) {
	var dt = PF('myDataTable');
	dt.tabCell = newTabCell;
	//dt.doCellEditRequest = newDoCellEditRequest;
};

/**
 * NewTab cell that skips to the next row and selects the next editable wrvu
 */
var newTabCell = function(cell, forward) {
	console.log("in newTabCell");
    //var targetCell = forward ? cell.next() : cell.prev();
    //console.log(targetCell.html());
    //if(targetCell.length == 0) {
        var tabRow = forward ? cell.parent().next() : cell.parent().prev();
        targetCell = forward ? tabRow.children('td.ui-editable-column:first') : tabRow.children('td.ui-editable-column:last');
   // }

    this.showCellEditor(targetCell);
};

/**
 * Set async=false
 */
var newDoCellEditRequest = function(cell) {
	console.log("in newDoCellEditRequest");
	var rowMeta = this.getRowMeta(cell.closest('tr')),
    cellEditor = cell.children('.ui-cell-editor'),
    cellEditorId = cellEditor.attr('id'),
    cellIndex = cell.index(),
    cellInfo = rowMeta.index + ',' + cellIndex,
    $this = this;

    var options = {
        source: this.id,
        process: this.id,
        update: this.id,
        async: "false",
        params: [{name: this.id + '_encodeFeature', value: true},
                 {name: this.id + '_cellInfo', value: cellInfo},
                 {name: cellEditorId, value: cellEditorId}],
        onsuccess: function(responseXML, status, xhr) {
            PrimeFaces.ajax.Response.handle(responseXML, status, xhr, {
                    widget: $this,
                    handle: function(content) {
                        cellEditor.children('.ui-cell-editor-output').html(content);
                    }
                });

            return true;
        },
        oncomplete: function(xhr, status, args) {                            
            if(args.validationFailed)
                cell.addClass('ui-state-error');
            else {
                //$this.viewMode(cell);
                //$this.tabCell(cell, true);
                //$this.currentCell = cell.ne;
                //console.log("completed: " + cell.html());
            }
        }
    };

    if(this.hasBehavior('cellEdit')) {
        this.cfg.behaviors['cellEdit'].call(this, options);
    }
    else {
        PrimeFaces.ajax.Request.handle(options);
    }
};