package mobi.dende.simpletimesheet.conveter;

public interface ExportListener {
    public void onProgress(int value, int total);

    public void onDone(String localSaved);

    public void onError();
}
