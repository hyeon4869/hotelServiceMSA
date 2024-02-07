<template>
    <v-card outlined>
        <v-card-title>
            PayCancel
        </v-card-title>

        <v-card-text>
            <String label="RoomId" v-model="value.roomId" :editMode="editMode"/>
        </v-card-text>

        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn
                    color="primary"
                    text
                    @click="payCancel"
            >
                PayCancel
            </v-btn>
            
            <v-btn
                    color="primary"
                    text
                    @click="close"
            >
                Close
            </v-btn>
        </v-card-actions>
    </v-card>

</template>

<script>
   
    export default {
        name: 'PayCancelCommand',
        components:{},
        props: {},
        data: () => ({
            editMode: true,
            value: {},
        }),
        created() {
            this.value.roomId = '';
        },
        watch: {
        },
        methods: {
            payCancel() {
                this.$emit('payCancel', this.value);
            },
            close() {
                this.$emit('closeDialog');
            },
            change() {
                this.$emit('input', this.value);
            },
        }
    }
</script>

