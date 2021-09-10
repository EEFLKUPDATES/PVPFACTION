package fr.eeflkpf.launcher.launch;

import fr.theshark34.openlauncherlib.external.ExternalLaunchProfile;
import fr.theshark34.openlauncherlib.external.ExternalLauncher;
import fr.theshark34.openlauncherlib.minecraft.*;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.supdate.SUpdate;
import fr.eeflkpf.launcher.LauncherFrame;
import fr.theshark34.supdate.application.integrated.FileDeleter;
import net.lingala.zip4j.ZipFile;

import java.io.*;

public class Launcher {

    public static final GameVersion EEFLKPF_VERSION = new GameVersion("1.12.2", GameType.V1_8_HIGHER);
    public static final GameInfos EEFLKPF_INFOS = new GameInfos("eeflkpf", EEFLKPF_VERSION, new GameTweak[] {GameTweak.FORGE});
    public static final File EEFLK_DIR = EEFLKPF_INFOS.getGameDir();

    private static AuthInfos authInfos;
    private static Thread updateThread;

    public static void auth(String username) throws Exception {

        try {
            update();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
//        AuthResponse rep = authenticator.authenticate(AuthAgent.MINECRAFT, username, "", "");
        authInfos = new AuthInfos(username, "", "");

        try {
            launch();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void update() throws Exception {

        SUpdate EEFLK_UPD = new SUpdate("https://eeflk-updates.herokuapp.com/", EEFLK_DIR);
        EEFLK_UPD.addApplication(new FileDeleter());

        updateThread = new Thread() {

            private int currentbar;
            private int maxbar;

            @Override
            public void run() {

                while(!this.isInterrupted()) {

                    currentbar = (int) ((BarAPI.getNumberOfTotalDownloadedBytes()/1000+BarAPI.getNumberOfDownloadedFiles()/100));
                    maxbar = (int) ((BarAPI.getNumberOfTotalBytesToDownload()/1000+BarAPI.getNumberOfFileToDownload()/100));

                    LauncherFrame.getInstance().getProgressBar().setValue(currentbar);
                    LauncherFrame.getInstance().getProgressBar().setMaximum(maxbar);

                }

            }

        };

        try{

            updateThread.start();
            EEFLK_UPD.start();

        }catch (Exception e) {

            interruptThread();

        }

        interruptThread();

        Thread.sleep(200L);

        File ancMods = new File(EEFLK_DIR.getPath()+"/zips/mods.zip");


        File newMods = new File(EEFLK_DIR.getPath()+"/mods");

        zipToFile(ancMods, newMods.getPath());

    }

    public static void launch() throws IOException {

        try {

            ExternalLaunchProfile profile = MinecraftLauncher.createExternalProfile(EEFLKPF_INFOS, GameFolder.BASIC, authInfos);
            ExternalLauncher launcher = new ExternalLauncher(profile);

            Process p = launcher.launch();

            try {

                Thread.sleep(5000L);

            }catch (InterruptedException e) {

            }

            LauncherFrame.getInstance().setVisible(false);

            try {

                p.waitFor();

            }catch (InterruptedException e) {

            }

            System.exit(0);

        }catch (Exception e) {

        }

    }

    public static void NormalExit() {

        try {

            Thread.sleep(532L);

        }catch(InterruptedException e) {

        }

        System.exit(0);

    }

    public static void interruptThread() {

        updateThread.interrupt();

    }

    public static void zipToFile(File source, String target) throws IOException {

        new ZipFile(source).extractAll(target);

    }

}
