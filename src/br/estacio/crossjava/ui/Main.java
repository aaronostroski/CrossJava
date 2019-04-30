package br.estacio.crossjava.ui;

import br.estacio.crossjava.model.Usuario;
import br.estacio.crossjava.service.UsuarioService;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.JSValue;
import com.teamdev.jxbrowser.chromium.events.FinishLoadingEvent;
import com.teamdev.jxbrowser.chromium.events.LoadAdapter;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.concurrent.atomic.AtomicReference;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Main {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Cross Java Application");

        JButton btn_register = new JButton("Novo Registro");
        btn_register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Usuario usuario = criarUsuario(frame);
                JOptionPane.showMessageDialog(null, usuario.toString());
            }
        });

        JPanel panel = new JPanel();
        panel.add(btn_register);
        frame.add(panel, BorderLayout.CENTER);

        // quando clicar em sair, matar a aplicação
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 80);
        // serve para renderizar no centro
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    public static Usuario criarUsuario(JFrame frame) {
        final AtomicReference<Usuario> result = new AtomicReference<>();
        
        final Browser browser = new Browser(); 
        BrowserView view = new BrowserView(browser);
        
        
        
        File registro = new File("registro.html");
        browser.loadURL("file://" + registro.getAbsolutePath());
        
        final JDialog dialog = new JDialog(frame, "Novo usuário", true);
        
        browser.addLoadListener(new LoadAdapter() {
            @Override
            public void onFinishLoadingFrame(FinishLoadingEvent event) {
                // verifica se é o frame principal
                if(event.isMainFrame()){
                    JSValue window = browser.executeJavaScriptAndReturnValue("window");
                    window.asObject().setProperty("UsuarioService", new UsuarioService(dialog, result));
                   }
            }
        });
        
        dialog.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        dialog.add(view, BorderLayout.CENTER);
        dialog.setSize(500, 500);
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        dialog.addWindowListener(new WindowAdapter(){
            
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                browser.dispose();
                dialog.setVisible(false);
                dialog.dispose();
            }
        });
        
        return result.get();
    }
}

//    public static void main(String[] args) {
//        // Motor de rederização
//        Browser browser = new Browser();
//        // View do motor
//        BrowserView view = new BrowserView(browser);
//        
//        JFrame frame = new JFrame("Cross Java Application");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(500, 500);
//        frame.setLocationRelativeTo(null);
//        frame.add(view, BorderLayout.CENTER);
//        frame.setVisible(true);
//        
//        // browser.loadHTML("<html><body><h1>Cross Java Application</h1></body></html>");;
//        browser.loadURL("https://g1.globo.com");
//    }
    
//}

/**
 * Diferença entre Listener e Adapter:
 * Adapter é classe e só tem que sobrescrever o que quiser usar.
 * Listener tem que implementar todos os métodos.
 */
