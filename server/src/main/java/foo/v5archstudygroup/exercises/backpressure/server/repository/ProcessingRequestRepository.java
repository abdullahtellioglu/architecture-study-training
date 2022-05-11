package foo.v5archstudygroup.exercises.backpressure.server.repository;

import foo.v5archstudygroup.exercises.backpressure.messages.Messages;

public interface ProcessingRequestRepository {


    void save(Messages.ProcessingRequest request);

    Messages.ProcessingRequest getAndDelete();


}
