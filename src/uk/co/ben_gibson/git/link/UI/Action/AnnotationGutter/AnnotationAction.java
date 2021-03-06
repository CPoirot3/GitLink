package uk.co.ben_gibson.git.link.UI.Action.AnnotationGutter;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vcs.annotate.FileAnnotation;
import com.intellij.openapi.vcs.annotate.UpToDateLineNumberListener;
import com.intellij.openapi.vcs.history.VcsRevisionNumber;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import uk.co.ben_gibson.git.link.Git.Commit;
import uk.co.ben_gibson.git.link.UI.Action.Action;
import uk.co.ben_gibson.git.link.Url.Handler.UrlHandler;

abstract public class AnnotationAction extends Action implements UpToDateLineNumberListener
{
    abstract UrlHandler urlHandler();

    private FileAnnotation annotation;
    private int lineNumber = -1;

    AnnotationAction(@NotNull FileAnnotation annotation)
    {
        this.annotation = annotation;
    }

    public void actionPerformed(Project project, AnActionEvent event)
    {
        if (this.lineNumber < 0) {
            return;
        }

        VirtualFile file = event.getData(CommonDataKeys.VIRTUAL_FILE);

        VcsRevisionNumber revisionNumber = this.annotation.getLineRevisionNumber(this.lineNumber);

        if (file == null || project == null || revisionNumber == null) {
            return;
        }

        Commit commit = new Commit(revisionNumber.asString());

        this.getManager().handleFile(this.urlHandler(), project, file, commit, null);
    }

    protected boolean shouldActionBeEnabled(AnActionEvent event)
    {
        return (event.getData(CommonDataKeys.VIRTUAL_FILE) != null);
    }


    @Override
    public void consume(Integer integer)
    {
        this.lineNumber = integer;
    }
}
