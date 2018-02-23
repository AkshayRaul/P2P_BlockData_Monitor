public class PushFile{
   String to;
   String from;
   String fileId;
   String fileType;

   PushFile(String to,String from,String fileId,String fileType){
       this.to=to;
       this.from=from;
       this.fileId=fileId;
       this.fileType=fileType;
   }
}
/*
static ArrayList<fileMetaData> fMD = new ArrayList<fileMetaData>();
    static ArrayList<Download> downloadList = new ArrayList<Download>();
*/
/*@Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        byte byt[]=bytes.toByteArray();
        if(byt[0]==0&&byt[1]==0){
            File file = new File("/storage/emulated/0/BlockStorage/" + (fMD.get(0).getFileName()));
            try {
                FileOutputStream fileOuputStream = new FileOutputStream(file);
                fileOuputStream.write(bytes.toByteArray());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

*/
/*@Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.i("toast", text);
        try {
            JSONObject reader = new JSONObject(text);
            if(reader.getString("type").compareToIgnoreCase("storage")==0){
                String fileName = reader.getString("fileName");
                long fileSize = reader.getLong("fileSize");
                String fileOwner = reader.getString("owner");
                String fileId = reader.getString("fileId");
                fMD.add(new fileMetaData(fileName, fileId, fileSize, fileOwner));
            }else{
                String fileName = reader.getString("fileName");
                long fileSize = reader.getLong("fileSize");
                String fileId = reader.getString("fileId");
                downloadList.add(new Download(fileName,fileId,fileSize));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

*/
