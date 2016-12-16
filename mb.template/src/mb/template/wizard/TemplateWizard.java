package mb.template.wizard;

import java.lang.reflect.InvocationTargetException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import mb.template.wizard.pages.TemplatePage;


/**
 * Main template wizard
 * 
 * @author mbaev
 */
public class TemplateWizard extends Wizard implements INewWizard
{
    
    private TemplatePage templatePage;



    public TemplateWizard()
    {
        super();

        setWindowTitle("Template Wizard");

        setNeedsProgressMonitor(true);
        this.templatePage = new TemplatePage();
    }



    @Override
    public void addPages()
    {
        addPage(templatePage);
    }



    @Override
    public boolean performFinish()
    {
        try
        {
            getContainer().run(true, true, new IRunnableWithProgress()
            {

                @Override
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException
                {
                    monitor.beginTask("Generate template", IProgressMonitor.UNKNOWN);

                    templatePage.copyTemplateAndReplaceyPlaceholders();
                }
            });
        }
        catch (InvocationTargetException e)
        {
            e.printStackTrace();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        return true;
    }



    @Override
    public void init(IWorkbench workbench, IStructuredSelection selection)
    {

    }



}
