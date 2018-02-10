package eduardo.kasio.todolist;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

    private EditText etTarefa;
    private Button btnAdicionar;
    private ListView idListView;
    private SQLiteDatabase bancoDados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();
        inicializarBancoDados();

        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //evento do botao.
            }
        });
    }

    private void inicializarBancoDados() {
        bancoDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas " +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR )");
    }

    private void inicializarComponentes() {

        etTarefa     = (EditText) findViewById(R.id.etTarefa);
        btnAdicionar = (Button) findViewById(R.id.btnAdicionar);
        idListView   = (ListView) findViewById(R.id.idListView);

        bancoDados = openOrCreateDatabase("apptarefas",MODE_PRIVATE,null);

    }
}
