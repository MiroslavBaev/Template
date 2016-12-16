/**
 * 
 */
package mb.template.wizard.table.editor;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import mb.template.placeholder.PlaceholderBean;

/**
 * Column Editing support used make table column editable
 * 
 * @author mbaev
 *
 */

public class ColumnEditingSupport extends EditingSupport
{
    private final TableViewer viewer;
    private final CellEditor editor;



    public ColumnEditingSupport(TableViewer viewer)
    {
        super(viewer);
        this.viewer = viewer;
        this.editor = new TextCellEditor(viewer.getTable());
    }



    @Override
    protected CellEditor getCellEditor(Object element)
    {
        return editor;
    }



    @Override
    protected boolean canEdit(Object element)
    {
        return true;
    }



    @Override

    protected Object getValue(Object element)
    {

        return ((PlaceholderBean) element).getValue();

    }



    @Override

    protected void setValue(Object element, Object userInputValue)
    {

        ((PlaceholderBean) element).setValue(String.valueOf(userInputValue));

        viewer.update(element, null);

    }

}
