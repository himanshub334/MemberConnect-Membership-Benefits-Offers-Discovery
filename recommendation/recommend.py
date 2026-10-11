"""Simple hybrid offer ranking baseline for demo data; not a production recommender."""
import argparse
import pandas as pd

def recommend(customer_id: str, redemptions: pd.DataFrame, offers: pd.DataFrame, limit: int = 3) -> pd.DataFrame:
    mine = redemptions[redemptions.customer_id == customer_id]
    if mine.empty:
        ranked = offers.copy()
        ranked["score"] = 0.0
        return ranked.head(limit)
    categories = mine.category.value_counts(normalize=True).to_dict()
    # Co-redemption similarity: categories frequently redeemed by customers with overlapping history.
    my_ids = set(mine.offer_id.astype(str))
    peers = redemptions[redemptions.customer_id != customer_id]
    peer_overlap = peers.groupby("customer_id").offer_id.apply(lambda xs: len(my_ids.intersection(set(xs.astype(str)))))
    peer_ids = set(peer_overlap[peer_overlap > 0].index)
    peer_categories = peers[peers.customer_id.isin(peer_ids)].category.value_counts(normalize=True).to_dict()
    ranked = offers.copy()
    ranked["category_score"] = ranked.category.map(categories).fillna(0.0)
    ranked["similar_member_score"] = ranked.category.map(peer_categories).fillna(0.0)
    already = set(mine.offer_id.astype(str))
    ranked = ranked[~ranked.offer_id.astype(str).isin(already)].copy()
    ranked["score"] = 0.65 * ranked.category_score + 0.35 * ranked.similar_member_score
    return ranked.sort_values(["score", "title"], ascending=[False, True]).head(limit).drop(columns=["category_score", "similar_member_score"])

def main():
    parser = argparse.ArgumentParser()
    parser.add_argument("--customer", default="customer-1")
    parser.add_argument("--redemptions", default="sample_redemptions.csv")
    parser.add_argument("--offers", default="sample_offers.csv")
    parser.add_argument("--limit", type=int, default=3)
    args = parser.parse_args()
    redemptions = pd.read_csv(args.redemptions)
    offers = pd.read_csv(args.offers)
    print(recommend(args.customer, redemptions, offers, args.limit).to_string(index=False))
if __name__ == "__main__": main()
