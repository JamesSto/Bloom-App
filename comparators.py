#returns value from 0 to 1 based on strength of comparison

class Comparator:
    @staticmethod
    def compareInterests(interests1, interestRating1, interests2, interestRating2):
        numMatches = 0
        score = 0
        for item in interests1:
            if item in interests2:
                numMatches += 1
                score += (interestRating1[interests1.index(item)]
                    + interestRating2[interests2.index(item)])

        return score/(numMatches * 10.0)            

    @staticmethod
    def compareRating(rating1, rating2):
        return (4.0 - abs(rating1 - rating2))/4.0

    @staticmethod
    def compareBinary(val1, val2):
        return 1.0 if val1 == val2 else 0.0

    @staticmethod
    def compareBubbleSelection(dict1, dict2):
        return sum(5.0 - abs(dict1[val] - dict2[val]) for val in dict1.keys())/(1.0 * len(dict1))
