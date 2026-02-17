package dbfileorga;

public class MitgliederDBOrdered extends MitgliederDB {

    public MitgliederDBOrdered() {
        super(true);
    }
    
    /**
	 * Returns the record matching the record number
	 * @param recNum the term to search for
	 * @return the record matching the search term
	 */
    @Override
	public Record read(int recNum){
		//TODO implement
		int blockNum = getBlockNumOfRecord(recNum);

		if (blockNum == -1)	return null; //record not found

		int relativePos = recNum;//calculate the position of the record in the block
		for (int i = 0; i < blockNum; i++) {
			relativePos -= db[i].getNumberOfRecords();
		}

		return db[blockNum].getRecord(relativePos);
	}
	
	/**
	 * Returns the number of the first record that matches the search term
	 * @param searchTerm the term to search for
	 * @return the number of the record in the DB -1 if not found
	 */
    @Override
	public int findPos(String searchTerm){
		//TODO implement
		//no record matching the search term found
		return -1;
	}
	
	/**
	 * Inserts the record into the file and returns the record number
	 * @param record
	 * @return the record number of the inserted record
	 */
    @Override
	public int insert(Record record){
		//TODO implement
		//find position of insertion
        int insertionPos = 1;
        while ( Integer.parseInt(read(insertionPos).getAttribute(1)) < Integer.parseInt(record.getAttribute(1)) ) {
            insertionPos++;
        }

        
	}
	
	/**
	 * Deletes the record specified 
	 * @param numRecord number of the record to be deleted
	 */
    @Override
	public void delete(int numRecord){
		//TODO implement
	}
	
	/**
	 * Replaces the record at the specified position with the given one.
	 * @param numRecord the position of the old record in the db
	 * @param record the new record
	 * 
	 */
    @Override
	public void modify(int numRecord, Record record){
		//TODO
	}

}
