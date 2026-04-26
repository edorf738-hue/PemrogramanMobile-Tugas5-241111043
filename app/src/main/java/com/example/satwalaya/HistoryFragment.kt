package com.example.satwalaya

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.satwalaya.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment(R.layout.fragment_history) {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentHistoryBinding.bind(view)

        showActive()

        binding.tabActive.setOnClickListener { showActive() }
        binding.tabCompleted.setOnClickListener { showCompleted() }
        binding.tabCancelled.setOnClickListener { showCancelled() }
    }

    private fun resetTabs() {
        binding.tabActive.background = null
        binding.tabCompleted.background = null
        binding.tabCancelled.background = null

        binding.tabActive.setTypeface(null, Typeface.NORMAL)
        binding.tabCompleted.setTypeface(null, Typeface.NORMAL)
        binding.tabCancelled.setTypeface(null, Typeface.NORMAL)
    }

    private fun showActive() {
        resetTabs()
        binding.tabActive.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_history_tab_active)
        binding.tabActive.setTypeface(null, Typeface.BOLD)

        val activeBooking = BookingStore.bookings.firstOrNull { it.status == "Active" }

        if (activeBooking == null) {
            binding.emptyStateContainer.visibility = View.VISIBLE
            binding.bookingListContainer.visibility = View.GONE

            binding.emptyIcon.text = "🗓️"
            binding.emptyTitle.text = "No active bookings"
            binding.emptySubtitle.text = "Book a service to get started!"
            binding.browseServiceButton.visibility = View.VISIBLE
        } else {
            binding.emptyStateContainer.visibility = View.GONE
            binding.bookingListContainer.visibility = View.VISIBLE

            binding.tvServiceName.text = activeBooking.serviceName
            binding.tvPet.text = "${activeBooking.petName} (${activeBooking.petType})"
            binding.tvDate.text = "📅  ${activeBooking.startDate} - ${activeBooking.endDate}"
            binding.tvPrice.text = formatRupiah(activeBooking.totalPrice)
            binding.tvStatus.text = "Confirmed"
            binding.btnCancel.visibility = View.VISIBLE

            binding.btnCancel.setOnClickListener {
                activeBooking?.status = "Cancelled"
                showCancelled()
            }
        }
    }

    private fun showCompleted() {
        resetTabs()
        binding.tabCompleted.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_history_tab_active)
        binding.tabCompleted.setTypeface(null, Typeface.BOLD)

        binding.bookingListContainer.visibility = View.GONE
        binding.emptyStateContainer.visibility = View.VISIBLE

        binding.emptyIcon.text = "✅"
        binding.emptyTitle.text = "No completed bookings"
        binding.emptySubtitle.text = "Your completed bookings will appear here"
        binding.browseServiceButton.visibility = View.GONE
    }

    private fun showCancelled() {
        resetTabs()
        binding.tabCancelled.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_history_tab_active)
        binding.tabCancelled.setTypeface(null, Typeface.BOLD)

        binding.bookingListContainer.visibility = View.GONE
        binding.emptyStateContainer.visibility = View.VISIBLE

        binding.emptyIcon.text = "❌"
        binding.emptyTitle.text = "No cancelled bookings"
        binding.emptySubtitle.text = "Great! Keep it that way!"
        binding.browseServiceButton.visibility = View.GONE
    }

    private fun formatRupiah(amount: Int): String {
        return "Rp " + "%,d".format(amount).replace(",", ".")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}