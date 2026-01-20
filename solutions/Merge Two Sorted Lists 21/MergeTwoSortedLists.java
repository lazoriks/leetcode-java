/*
You are given the heads of two sorted linked lists list1 and list2.

Merge the two lists into one sorted list. The list should be made by splicing together the nodes of the first two lists.

Return the head of the merged linked list.
 */
class ListNode {
    int val;
    ListNode next;
    ListNode(int x) { val = x; }
}

public class MergeTwoSortedLists {
    
        public ListNode mergeTwoLists(ListNode list1, ListNode list2) {
            // Фіктивна голова для спрощення
            ListNode dummy = new ListNode(-1);
            ListNode current = dummy;
            
            while (list1 != null && list2 != null) {
                if (list1.val <= list2.val) {
                    current.next = list1;
                    list1 = list1.next;
                } else {
                    current.next = list2;
                    list2 = list2.next;
                }
                current = current.next;
            }
            
            // Додаємо залишок
            current.next = (list1 != null) ? list1 : list2;
            
            return dummy.next;
        }
}
// Час: O(n+m), Пам'ять: O(1)
