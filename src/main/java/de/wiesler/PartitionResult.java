package de.wiesler;

public final class PartitionResult {
    public final int num_buckets;
    public final boolean equal_buckets;

    /*@ public normal_behaviour
      @ ensures this.num_buckets == num_buckets && this.equal_buckets == equal_buckets;
      @
      @ assignable \nothing;
      @*/
    public PartitionResult(int num_buckets, boolean equal_buckets) {
        this.num_buckets = num_buckets;
        this.equal_buckets = equal_buckets;
    }
}
