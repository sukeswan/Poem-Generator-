class MyHashtable implements DictionaryInterface {
	protected int tableSize;
	protected int size;
	protected MyLinkedList[] table;

	protected class Entry {
		String key;
		Object value;

		Entry(String key, Object value) {
			this.key = key;
			this.value = value;
		}
	}

	// constructor for hash table
	public MyHashtable(int tableSize) {
		this.tableSize = tableSize;
		table = new MyLinkedList[tableSize];
		size = 0;
	}

	// Returns the size of the biggest bucket (most collisions) in the hashtable.
	public int biggestBucket() {
		int biggestBucket = 0;
		for (int i = 0; i < table.length; i++) {
			// Loop through the table looking for non-null locations.
			if (table[i] != null) {
				// If you find a non-null location, compare the bucket size against the largest
				// bucket size found so far. If the current bucket size is bigger, set
				// biggestBucket
				// to this new size.
				MyLinkedList bucket = table[i];
				if (biggestBucket < bucket.size())
					biggestBucket = bucket.size();
			}
		}
		return biggestBucket; // Return the size of the biggest bucket found.
	}

	// Returns the average bucket length. Gives a sense of how many collisions are
	// happening overall.
	public float averageBucket() {
		float bucketCount = 0; // Number of buckets (non-null table locations)
		float bucketSizeSum = 0; // Sum of the size of all buckets
		for (int i = 0; i < table.length; i++) {
			// Loop through the table
			if (table[i] != null) {
				// For a non-null location, increment the bucketCount and add to the
				// bucketSizeSum
				MyLinkedList bucket = table[i];
				bucketSizeSum += bucket.size();
				bucketCount++;
			}
		}

		// Divide bucketSizeSum by the number of buckets to get an average bucket
		// length.
		return bucketSizeSum / bucketCount;
	}

	public String toString() {
		String s = "";
		for (int tableIndex = 0; tableIndex < tableSize; tableIndex++) {
			if (table[tableIndex] != null) {
				MyLinkedList bucket = table[tableIndex];
				for (int listIndex = 0; listIndex < bucket.size(); listIndex++) {
					Entry e = (Entry) bucket.get(listIndex);
					s = s + "key: " + e.key + ", value: " + e.value + "\n";
				}
			}
		}
		return s;
	}

	@Override
	public boolean isEmpty() {
		// if size is 0 the list is empty
		return size == 0;
	}

	@Override
	public int size() {
		// return number of key/value pairs stored in the hash table
		return size;
	}

	@Override
	public Object put(String key, Object value) {
		// get hashcode and convert it to an array index
		int hashCode = key.hashCode();
		int arrayIndex = Math.abs(hashCode) % tableSize;
		// if array index is null
		if (table[arrayIndex] == null) {
			// create a new bucket and store new entry in bucket
			MyLinkedList bucket = new MyLinkedList();
			Entry newEntry = new Entry(key, value);
			bucket.add(0, newEntry);
			// add bucket to table
			table[arrayIndex] = bucket;
			// increase the number of unique keys stored
			size++;
			return null;
		} else { // bucket with array index already exists
			// create currentBucket variable
			MyLinkedList currentBucket = table[arrayIndex];
			// traverse through the current bucket
			for (int i = 0; i < currentBucket.size(); i++) {
				// get the current entry and index i
				Entry currentEntry = (Entry) currentBucket.get(i);
				// compare current entry key to parameter key
				if (currentEntry.key.equals(key)) { // if they are equal
					// store old value so it can be returned
					Object oldValue = currentEntry.value;
					// replace value with updated value from paramater
					currentEntry.value = value;
					// update the bucket in the table
					table[arrayIndex] = currentBucket;
					return oldValue;
				}
				// key does not exist already to add a new entry to the bucket
				else {
					// create new entry with parameters
					Entry newEntry = new Entry(key, value);
					// add it to beginning of the current bucket
					currentBucket.add(0, newEntry);
					// increase size of unique keys
					size++;
					// update the bucket in the table
					table[arrayIndex] = currentBucket;
					return null;
				}
			}
		}
		return null;
	}

	@Override
	public Object get(String key) {
		// get hashcode and convert it to an array index
		int hashCode = key.hashCode();
		int arrayIndex = Math.abs(hashCode) % tableSize;
		// check to see if the array in the table is empty
		if (table[arrayIndex] == null) {
			return null;
		}
		// the array in the table has a linked list
		else {
			// create variable for current bucket
			MyLinkedList currentBucket = table[arrayIndex];
			// traverse through the current bucket
			for (int i = 0; i < currentBucket.size(); i++) {
				// create variable for current entry
				Entry currentEntry = (Entry) currentBucket.get(i);
				// if the current entry key equals parameter key return
				// the value at the current entry
				if (currentEntry.key.equals(key)) {
					return currentEntry.value;
				}
			}
		}
		return null;
	}

	@Override
	public void remove(String key) {
		// get hashcode and convert it to an array index
		int hashCode = key.hashCode();
		int arrayIndex = Math.abs(hashCode) % tableSize;
		// make current bucket variable
		MyLinkedList currentBucket = table[arrayIndex];
		// if the current bucket is not empty
		if (currentBucket != null) {
			// traverse through the bucket
			for (int i = 0; i < currentBucket.size(); i++) {
				Entry currentEntry = (Entry) currentBucket.get(i);
				// if current entry key is equal to parameter key
				if (currentEntry.key.equals(key)) {
					// delete key and decrease size;
					currentBucket.remove(i);
					size--;
				}
			}
		}
	}

	@Override
	public void clear() {
		table = new MyLinkedList[tableSize];
		size = 0;

	}

	@Override
	public String[] getKeys() {
		// make string array to store keys
		String[] keys = new String[size];
		// traverse through the table
		// counter to keep track of where to add keys to keys array
		int counter = 0;
		for (int i = 0; i < tableSize; i++) {
			// make current bucket varaible
			MyLinkedList currentBucket = table[i];
			// if the current bucket is not null traverse through it
			if (currentBucket != null) {
				for (int j = 0; j < currentBucket.size(); j++) {
					// get current entry
					Entry currentEntry = (Entry) currentBucket.get(j);
					// add key at current entry to keys array
					keys[counter] = currentEntry.key;
					// increase counter
					counter++;
				}
			}
		}
		return keys;
	}
}