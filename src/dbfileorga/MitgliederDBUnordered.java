package dbfileorga;

public class MitgliederDBUnordered extends MitgliederDB {

    public MitgliederDBUnordered() {
        super(false);
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
		int recNum = 1;
		//iterate over all blocks
		for(DBBlock block : db){
			//iterate over all records in the block
			for (Record rec : block){//1 = first record
				//search term matches record id
				if(rec.getAttribute(1).equals(searchTerm)) return recNum;

                //search term did not match, check next record
				recNum++;
			}
		}
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
		//unsorted: just append record at the end of next free block
		appendRecord(record);
		return getNumberOfRecords();
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
