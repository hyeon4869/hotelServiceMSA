<template>

    <v-card outlined>
        <v-card-title>
            MyPage # {{$route.params.id }}
        </v-card-title>

        <v-card-text>
            <div>
                <String label="RoomNumber" v-model="item.roomNumber" :editMode="editMode" @change="change" />
            </div>
            <div>
                <String label="CustomerId" v-model="item.customerId" :editMode="editMode" @change="change" />
            </div>
            <div>
                <String label="Status" v-model="item.status" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Date label="CheckInDate" v-model="item.checkInDate" :editMode="editMode" @change="change" />
            </div>
            <div>
                <Date label="CheckOutDate" v-model="item.checkOutDate" :editMode="editMode" @change="change" />
            </div>
            <div>
                <String label="RoomType" v-model="item.roomType" :editMode="editMode" @change="change" />
            </div>
        </v-card-text>
    </v-card>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'MyPageViewDetail',
    props: {
      value: Object,
    },
    data: () => ({
        item : [],
    }),
    async created() {
      var params = this.$route.params;
      var temp = await axios.get(axios.fixUrl('/myPages/' + params.id))

      this.item = temp.data;

    },
    methods: {
    }
  }
</script>

