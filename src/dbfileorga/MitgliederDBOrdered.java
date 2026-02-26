package dbfileorga;

public class MitgliederDBOrdered extends MitgliederDB {

    public MitgliederDBOrdered() {
        super(true);
    }

    /**
     * Returns the record matching the record number
     *
     * @param recNum the term to search for
     * @return the record matching the search term
     */
    @Override
    public Record read(int recNum) {
        if (recNum < 1 || recNum > getNumberOfRecords()) return null;//invalid record number

        int blockNum = getBlockNumOfRecord(recNum);

        if (blockNum == -1) return null; //record not found

        int relativePos = calcRelativeRecNumInBlock(recNum, blockNum);

        return db[blockNum].getRecord(relativePos);
    }

    /**
     * Returns the number of the first record that matches the search term
     *
     * @param searchTerm the term to search for
     * @return the number of the record in the DB -1 if not found
     */
    @Override
    public int findPos(String searchTerm) {
        if (searchTerm == null) return -1;//invalid search term
        
        int recNum = 1;
        //iterate over all blocks
        for (DBBlock block : db) {
            //iterate over all records in the block
            for (Record rec : block) {
                //search term matches record id
                if (rec.getAttribute(1).equals(searchTerm)) return recNum;

                //search term did not match, check next record
                recNum++;
            }
        }
        //no record matching the search term found
        return -1;
    }

    /**
     * Inserts the record into the file and returns the record number
     *
     * @param record
     * @return the record number of the inserted record
     */
    @Override
    public int insert(Record record) {
        //record too long for one block
        if (record.length() > DBBlock.BLOCKSIZE) return -1;

        //if db is empty jsut insert record
        if (getNumberOfRecords() == 0) {
            appendRecord(record);
            return 1;
        }

        //find position of insertion
        int insertionPos = 1;
        while (Integer.parseInt(read(insertionPos).getAttribute(1)) < Integer.parseInt(record.getAttribute(1))) {
            insertionPos++;

            //record to be appended at the end
            if (insertionPos > getNumberOfRecords()) {
                appendRecord(record);
                return insertionPos;
            }
        }

        //get insertion position in current block
        int blockNum = getBlockNumOfRecord(insertionPos);

        int firstRecNumInBlock = calcFirstRecNumInBlock(blockNum);

        //temporarily store all records starting from insertion block
        Record[] tempList = new Record[getNumberOfRecords() - firstRecNumInBlock + 1];
        for (int i = 0; i < tempList.length; i++) {
            tempList[i] = read(firstRecNumInBlock + i);
        }

        //delete content beginning with insertion block
        for (int i = blockNum; i < db.length; i++) {
            db[i].delete();
        }

        //insert records before new record
        int localInsertPos = insertionPos - firstRecNumInBlock;
        for (int i = 0; i < localInsertPos; i++) {
            if (appendRecord(tempList[i]) == -1) return -1; //insertion failed
        }

        //insert new record at insertion position
        if (appendRecord(record) == -1) return -1; //insertion failed

        //reinsert all records after new record
        for (int i = localInsertPos; i < tempList.length; i++) {
            if (appendRecord(tempList[i]) == -1) return -1; //insertion failed
        }

        return insertionPos;//return position of inserted record
    }

    /**
     * Deletes the record specified
     *
     * @param numRecord number of the record to be deleted
     */
    @Override
    public void delete(int numRecord) {
        if (numRecord < 1 || numRecord > getNumberOfRecords()) return;//invalid record number

        int blockNum = getBlockNumOfRecord(numRecord);

        int firstRecNumInBlock = calcFirstRecNumInBlock(blockNum);

        //temporarily store all records starting from insertion block leaving out the record to be deleted
        Record[] tempList = new Record[getNumberOfRecords() - firstRecNumInBlock+1];
        for (int i = 0; i < tempList.length; i++) {
            int currentRecNum = firstRecNumInBlock + i;
            if (currentRecNum == numRecord) continue;//skip record to be deleted
            tempList[i] = read(currentRecNum);
        }

        //delete all blocks
        for (int i = blockNum; i < db.length; i++) {
            db[i].delete();
        }

        //reinsert all records
        for (Record rec : tempList) {
            if (rec != null) //sort out deleted record
                appendRecord(rec);//insertion failed
        }
    }

    /**
     * Replaces the record at the specified position with the given one.
     *
     * @param numRecord the position of the old record in the db
     * @param record    the new record
     *
     */
    @Override
    public void modify(int numRecord, Record record) {
        //record too long for one block
        if (record.length() > DBBlock.BLOCKSIZE) return;

        //invalid record number
        if (numRecord < 1 || numRecord > getNumberOfRecords()) return;

        //special case: length exactly matches
        if (record.length() == read(numRecord).length()) {
            int blockNum = getBlockNumOfRecord(numRecord);

            calcRelativeRecNumInBlock(numRecord, blockNum);

            //only delete affected block
            Record[] tempList = new Record[db[blockNum].getNumberOfRecords()];
            for (int i = 0; i < tempList.length; i++) {
                if (i == numRecord) {
                    tempList[i] = record;//replace record at numRecord with new record instead of reading
                } else {
                    tempList[i] = read(numRecord);
                }
            }

            db[blockNum].delete();

            //reinsert all records
            for (Record rec : tempList) {
                appendRecord(rec);
            }
            return;
        }

        //if length does not match
        //delete and reinsert changed record
        delete(numRecord);
        insert(record);
    }


}
