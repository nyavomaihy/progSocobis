// package api.utils;

// import reservation.ReservationLibF;
// import api.dto.ReservationDTO;
// import java.util.ArrayList;
// import java.util.List;
// import bean.CGenUtil;
// import bean.ClassMAPTable;

// public class ReservationTransformer {
    
//     /**
//      * Transforme un tableau de ReservationLibF en tableau de ReservationDTO
//      */
//     public static ReservationDTO[] transformToDTO(ReservationLibF[] reservationLibs) {
//         if (reservationLibs == null) {
//             return new ReservationDTO[0];
//         }
        
//         List<ReservationDTO> dtoList = new ArrayList<>();
        
//         for (ReservationLibF reservationLib : reservationLibs) {
//             ReservationDTO dto = transformSingleToDTO(reservationLib);
//             dtoList.add(dto);
//         }
        
//         return dtoList.toArray(new ReservationDTO[0]);
//     }
    

//     /**
//      * Transforme un seul objet ReservationLibF en ReservationDTO
//      */
//     public static ReservationDTO transformSingleToDTO(ReservationLibF reservationLib) {
//         ReservationDTO dto = new ReservationDTO();
        
//         // Transformation des champs spécifiés
//         dto.setId(reservationLib.getId());
//         dto.setClient(reservationLib.getIdclientlib());
//         dto.setDateReservation(reservationLib.getDaty());
//         dto.setRemarque(reservationLib.getRemarque());
//         dto.setEtatPayment(reservationLib.getEtatpaymentlib());
//         dto.setEtatLogistique(reservationLib.getEtatlogistiquelib());
        
//         return dto;
//     }
    


//     public static ReservationDTO[] getReservations() throws Exception {
//         String query = "SELECT * FROM RESERVATION_ETATLIB_F WHERE DATY >= DATE '2026-01-01' AND DATY < DATE '2027-01-01'";
        
//         // Exécuter la requête
//         ReservationLibF[] reservations = (ReservationLibF[]) CGenUtil.rechercher((ClassMAPTable) new ReservationLibF(), query);
        
//         // Transformer en DTO
//         return transformToDTO(reservations);
//     }
// }
