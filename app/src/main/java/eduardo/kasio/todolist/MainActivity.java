package eduardo.kasio.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private EditText etTarefa;
    private Button btnAdicionar;
    private ListView idListView;
    private SQLiteDatabase bancoDados;
    private ArrayAdapter<String> itensAdaptador;
    private ArrayList<String> itens;
    private ArrayList<Integer> ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{

            inicializarComponentes();
            inicializarBancoDados();


            btnAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String s = etTarefa.getText().toString();
                    salvarTarefa(s);
                    etTarefa.setText("");
                }
            });

            //exibindo um AlertDialog para confirmação da remoção da tarefa.
            idListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final Integer posicao = i;

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Deseja remover a tarefa?");
                    builder.setTitle("ATENÇÃO!!!");

                    builder.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            removerTarefa(ids.get(posicao));
                        }
                    });
                    builder.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this,"NÃO FOI REMOVIDO!!",Toast.LENGTH_SHORT).show();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);//não permite que o usuário saida do dialog clicando em outro canto da tela.
                    dialog.show();
                }
            });

            //Removendo tarefa com um longo click em cima da mesma
            idListView.setLongClickable(true);
            idListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    removerTarefa(ids.get(i));
                    return true;
                }
            });


            //Recuperar as tarefas
            recuperarTarefas();


        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void inicializarComponentes() {
        etTarefa     = (EditText) findViewById(R.id.etTarefa);
        btnAdicionar = (Button) findViewById(R.id.btnAdicionar);
        idListView   = (ListView) findViewById(R.id.idListView);
        idListView = (ListView) findViewById(R.id.idListView);
    }

    private void inicializarBancoDados() {
        bancoDados = openOrCreateDatabase("apptarefas", MODE_PRIVATE, null);
        codigoSQL("CREATE TABLE IF NOT EXISTS tarefas(id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR ) ");
    }

    private void codigoSQL(String s){
        try{
            bancoDados.execSQL(s);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void salvarTarefa(String s){
        try{
            if(s.equals("")) {
                Toast.makeText(MainActivity.this, "Digite um texto válido...", Toast.LENGTH_SHORT).show();
            }else {
                codigoSQL("INSERT INTO tarefas (tarefa) VALUES('" + s + "') ");
                Toast.makeText(MainActivity.this,"Tarefa salva com sucesso!",Toast.LENGTH_SHORT).show();
                recuperarTarefas();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void recuperarTarefas(){
        try {
            Cursor cursor = bancoDados.rawQuery("SELECT * FROM tarefas ORDER BY id DESC", null);

            //recuperar os ids das colunas
            int indiceColunaId = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("tarefa");

            //criando o adaptador
            itens = new ArrayList<String>();
            ids   = new ArrayList<Integer>();
            itensAdaptador = new ArrayAdapter<String>(
                    getApplicationContext(),
                    android.R.layout.simple_list_item_1,
                    android.R.id.text1,
                    itens);


            idListView.setAdapter(itensAdaptador);


            //listar as tarefas
            cursor.moveToFirst();
            while ( cursor != null ){
                itens.add(cursor.getString(indiceColunaTarefa));
                ids.add(Integer.parseInt(cursor.getString(indiceColunaId)));
                cursor.moveToNext();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void removerTarefa(Integer id){
        try {
            codigoSQL("DELETE FROM tarefas WHERE id = "+id);
            Toast.makeText(MainActivity.this,"Removido com sucesso!",Toast.LENGTH_SHORT).show();
            recuperarTarefas();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
