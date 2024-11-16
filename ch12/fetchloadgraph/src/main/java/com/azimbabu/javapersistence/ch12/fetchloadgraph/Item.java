package com.azimbabu.javapersistence.ch12.fetchloadgraph;

import com.azimbabu.javapersistence.ch12.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@NamedEntityGraphs({
    @NamedEntityGraph // The default "Item" entity graph
    ,
    @NamedEntityGraph(
        name = "ItemSeller",
        attributeNodes = {
            @NamedAttributeNode("seller")
        }
    )
})
@Entity
public class Item {

  public static final String PROFILE_JOIN_SELLER = "JoinSeller";
  public static final String PROFILE_JOIN_BIDS = "JoinBids";

  @Id
  @GeneratedValue(generator = Constants.ID_GENERATOR)
  private Long id;

  @NotNull
  private String name;

  @NotNull
  private LocalDateTime auctionEnd;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  private User seller;

  @OneToMany(mappedBy = "item")
  private Set<Bid> bids = new HashSet<>();

  public Item() {
  }

  public Item(String name, LocalDateTime auctionEnd, User seller) {
    this.name = name;
    this.auctionEnd = auctionEnd;
    this.seller = seller;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(@NotNull String name) {
    this.name = name;
  }

  public LocalDateTime getAuctionEnd() {
    return auctionEnd;
  }

  public void setAuctionEnd(LocalDateTime auctionEnd) {
    this.auctionEnd = auctionEnd;
  }

  public User getSeller() {
    return seller;
  }

  public void setSeller(User seller) {
    this.seller = seller;
  }

  public Set<Bid> getBids() {
    return Collections.unmodifiableSet(bids);
  }

  public void addBid(Bid bid) {
    bids.add(bid);
  }
}
