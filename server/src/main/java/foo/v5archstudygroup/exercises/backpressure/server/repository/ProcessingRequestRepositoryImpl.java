package foo.v5archstudygroup.exercises.backpressure.server.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.ByteString;
import foo.v5archstudygroup.exercises.backpressure.messages.Messages;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class ProcessingRequestRepositoryImpl implements ProcessingRequestRepository{
    //Might be used other kinds of databases like mongo etc..
    private static final String PROCESS_FOLDER = System.getProperty("user.home") +"/process-requests";




    @PostConstruct
    public void createFolder(){

        File file = new File(PROCESS_FOLDER);
        if(!file.exists()){
            boolean mkdir = file.mkdir();
            if(!mkdir){
                throw new RuntimeException("Folder can not be created!");
            }
        }
    }


    @Override
    public void save(Messages.ProcessingRequest request) {
        try {
            File file = new File(PROCESS_FOLDER + "/"+ request.getUuid());
            Files.writeString(Path.of(file.toURI()), request.getPayload().toString());

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public Messages.ProcessingRequest getAndDelete() {
        File processFolder = new File(PROCESS_FOLDER);
        File[] files = processFolder.listFiles();
        if(files == null || files.length == 0){
            return null;
        }
        Messages.ProcessingRequest processingRequest = null;
        try {
            byte[] bytes = Files.readAllBytes(Path.of(files[0].toURI()));
            processingRequest = Messages.ProcessingRequest.newBuilder()
                                .setUuid(files[0].getName())
            .setPayload(ByteString.copyFrom(bytes))
            .build();

            files[0].delete();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return processingRequest;
    }
}
