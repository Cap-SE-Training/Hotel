function DataTableHelper(element, options, noSelection) {
    var self = this;

    self.element = element;
    self.dataTable = element.DataTable(options);
    self.selection = !noSelection || false;

    if (self.selection) {
        // Select row handler
        element.on('click', 'tr', function () {
            self.element.find('tr.selected').removeClass('selected');
            var id = self.dataTable.row(this).id();
            if (id !== self.selectedRowId) {
                $(this).addClass('selected');
                self.selectedRowId = self.dataTable.row(this).id();
            } else {
                self.selectedRowId = undefined;
            }

            $('button.controls').prop('disabled', self.selectedRowId === undefined);
        });
    }
}

_.extend(DataTableHelper.prototype, {
    getElement: function() {
        return this.element;
    },
    getDataTable: function() {
        return this.dataTable;
    },
    getSelectedRowId: function() {
        return this.selectedRowId;
    },
    getSelectedRowData: function() {
        if (!this.selectedRowId) {
            return;
        }

        return this.dataTable.row('#' + this.selectedRowId).data();
    }
});
