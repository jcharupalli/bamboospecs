import com.atlassian.bamboo.specs.api.BambooSpec;
import com.atlassian.bamboo.specs.api.builders.BambooKey;
import com.atlassian.bamboo.specs.api.builders.BambooOid;
import com.atlassian.bamboo.specs.api.builders.Variable;
import com.atlassian.bamboo.specs.api.builders.applink.ApplicationLink;
import com.atlassian.bamboo.specs.api.builders.permission.PermissionType;
import com.atlassian.bamboo.specs.api.builders.permission.Permissions;
import com.atlassian.bamboo.specs.api.builders.permission.PlanPermissions;
import com.atlassian.bamboo.specs.api.builders.plan.Job;
import com.atlassian.bamboo.specs.api.builders.plan.Plan;
import com.atlassian.bamboo.specs.api.builders.plan.PlanIdentifier;
import com.atlassian.bamboo.specs.api.builders.plan.Stage;
import com.atlassian.bamboo.specs.api.builders.plan.artifact.Artifact;
import com.atlassian.bamboo.specs.api.builders.plan.branches.BranchCleanup;
import com.atlassian.bamboo.specs.api.builders.plan.branches.PlanBranchManagement;
import com.atlassian.bamboo.specs.api.builders.plan.configuration.ConcurrentBuilds;
import com.atlassian.bamboo.specs.api.builders.project.Project;
import com.atlassian.bamboo.specs.api.builders.repository.VcsChangeDetection;
import com.atlassian.bamboo.specs.builders.repository.bitbucket.server.BitbucketServerRepository;
import com.atlassian.bamboo.specs.builders.repository.viewer.BitbucketServerRepositoryViewer;
import com.atlassian.bamboo.specs.builders.task.CheckoutItem;
import com.atlassian.bamboo.specs.builders.task.CommandTask;
import com.atlassian.bamboo.specs.builders.task.MavenTask;
import com.atlassian.bamboo.specs.builders.task.VcsCheckoutTask;
import com.atlassian.bamboo.specs.util.BambooServer;

@BambooSpec
public class PlanSpec {
    
    public Plan plan() {
        final Plan plan = new Plan(new Project())
                .key(new BambooKey("INFRASYSATLASSIAN"))
                .name("infrasys-atlassian1"),
            "infrasys-atlassian-test1-java-com",
            new BambooKey("INFRASYSATLASSIANTEST1JAVACOM")
            .enabled(false)
            .pluginConfigurations(new ConcurrentBuilds())
            .stages(new Stage("Default Stage")
                    .jobs(new Job("Default Job",
                            new BambooKey("JOB1"))
                            .artifacts(new Artifact()
                                    .name("DeployArtifact")
                                    .copyPattern("**/**")
                                    .shared(true)
                                    .required(true))
                            .tasks(new VcsCheckoutTask()
                                    .description("Source Code Checkout")
                                    .checkoutItems(new CheckoutItem().defaultRepository())
                                    .cleanCheckout(true),
                                new MavenTask()
                                    .description("Maven Deploy")
                                    .goal("clean deploy")
                                    .jdk("JDK 8 Latest")
                                    .executableLabel("Maven latest")
                                    .hasTests(true)
                                    .useMavenReturnCode(true),
                                new CommandTask()
                                    .description("Python Script - Build Metadata")
                                    .executable("Buildtools")
                                    .argument("-m infrasys_deployment_ocp4_python_com.builds ${bamboo.repotype} ${bamboo.planRepository.1.repositoryUrl}"))))
            .planRepositories(new BitbucketServerRepository()
                    .name("infrasys-atlassian-test1-java-com")
                    .repositoryViewer(new BitbucketServerRepositoryViewer())
                    .server(new ApplicationLink()
                            .name("Bitbucket")
                            .id("80dc838e-f4e7-3e5a-9d3d-21de33b1f7fd"))
                    .projectKey("INFRASYSATLASSIAN")
                    .repositorySlug("newtest")
                    .sshPublicKey("ssh-ed25519 AAAAC3NzaC1lZDI1NTE5AAAAIGTEPxMHgPbdFd4DdW2FId7ZlgIgsTtLLeWmBo2sCg1w http://bamboo8latest:8085")
                    .sshPrivateKey("BAMSCRT@0@0@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")
                    .sshCloneUrl("ssh://git@bitbucket:7999/infrasysatlassian/newtest.git")
                    .changeDetection(new VcsChangeDetection()))
            
            .variables(new Variable("repotype",
                    "git"))
            .planBranchManagement(new PlanBranchManagement()
                    .createForVcsBranch()
                    .delete(new BranchCleanup()
                        .whenRemovedFromRepositoryAfterDays(7)
                        .whenInactiveInRepositoryAfterDays(30))
                    .notificationForCommitters())
            .forceStopHungBuilds();
        return plan;
    }
    
    public PlanPermissions planPermission() {
        final PlanPermissions planPermission = new PlanPermissions(new PlanIdentifier("INFRASYSATLASSIAN", "INFRASYSATLASSIANTEST1JAVACOM"))
            .permissions(new Permissions()
                    .groupPermissions("bamboo-admin", PermissionType.EDIT, PermissionType.VIEW, PermissionType.ADMIN, PermissionType.BUILD, PermissionType.CLONE));
        return planPermission;
    }
    
    public static void main(String... argv) {
        //By default credentials are read from the '.credentials' file.
        BambooServer bambooServer = new BambooServer("http://bamboo8latest:8085/");
        final PlanSpec planSpec = new PlanSpec();
        
        final Plan plan = planSpec.plan();
        bambooServer.publish(plan);
        
        final PlanPermissions planPermission = planSpec.planPermission();
        bambooServer.publish(planPermission);
    }
}
